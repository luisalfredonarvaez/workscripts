package mvc.jswing.example;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class SwingMVCExample
{

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("MVC example");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(640, 300);
        mainFrame.setLocationRelativeTo(null);

        PersonService personService = new PersonServiceMock();

        DefaultListModel searchResultListModel = new DefaultListModel();
        DefaultListSelectionModel searchResultSelectionModel = new DefaultListSelectionModel();
        searchResultSelectionModel
                .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Document searchInput = new PlainDocument();

        PersonDetailsAction personDetailsAction = new PersonDetailsAction(
                searchResultSelectionModel, searchResultListModel);
        personDetailsAction.putValue(Action.NAME, "Person Details");

        Action searchPersonAction = new SearchPersonAction(searchInput,
                searchResultListModel, personService);
        searchPersonAction.putValue(Action.NAME, "Search");

        Container contentPane = mainFrame.getContentPane();

        JPanel searchInputPanel = new JPanel();
        searchInputPanel.setLayout(new BorderLayout());

        JTextField searchField = new JTextField(searchInput, null, 0);
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchField.addActionListener(searchPersonAction);

        JButton searchButton = new JButton(searchPersonAction);
        searchInputPanel.add(searchButton, BorderLayout.EAST);

        JList searchResultList = new JList();
        searchResultList.setModel(searchResultListModel);
        searchResultList.setSelectionModel(searchResultSelectionModel);

        JPanel searchResultPanel = new JPanel();
        searchResultPanel.setLayout(new BorderLayout());
        JScrollPane scrollableSearchResult = new JScrollPane(searchResultList);
        searchResultPanel.add(scrollableSearchResult, BorderLayout.CENTER);

        JPanel selectionOptionsPanel = new JPanel();

        JButton showPersonDetailsButton = new JButton(personDetailsAction);
        selectionOptionsPanel.add(showPersonDetailsButton);

        contentPane.add(searchInputPanel, BorderLayout.NORTH);
        contentPane.add(searchResultPanel, BorderLayout.CENTER);
        contentPane.add(selectionOptionsPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

}

class PersonDetailsAction extends AbstractAction {

    private static final long serialVersionUID = -8816163868526676625L;

    private ListSelectionModel personSelectionModel;
    private DefaultListModel personListModel;

    public PersonDetailsAction(ListSelectionModel personSelectionModel,
                               DefaultListModel personListModel) {
        boolean unsupportedSelectionMode = personSelectionModel
                .getSelectionMode() != ListSelectionModel.SINGLE_SELECTION;
        if (unsupportedSelectionMode) {
            throw new IllegalArgumentException(
                    "PersonDetailAction can only handle single list selections. "
                            + "Please set the list selection mode to ListSelectionModel.SINGLE_SELECTION");
        }
        this.personSelectionModel = personSelectionModel;
        this.personListModel = personListModel;
        personSelectionModel
                .addListSelectionListener(new ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent e) {
                        ListSelectionModel listSelectionModel = (ListSelectionModel) e
                                .getSource();
                        updateEnablement(listSelectionModel);
                    }
                });
        updateEnablement(personSelectionModel);
    }

    public void actionPerformed(ActionEvent e) {
        int selectionIndex = personSelectionModel.getMinSelectionIndex();
        PersonElementModel personElementModel = (PersonElementModel) personListModel
                .get(selectionIndex);

        Person person = personElementModel.getPerson();
        String personDetials = createPersonDetails(person);

        JOptionPane.showMessageDialog(null, personDetials);
    }

    private String createPersonDetails(Person person) {
        return person.getId() + ": " + person.getFirstName() + " "
                + person.getLastName();
    }

    private void updateEnablement(ListSelectionModel listSelectionModel) {
        boolean emptySelection = listSelectionModel.isSelectionEmpty();
        setEnabled(!emptySelection);
    }

}

class SearchPersonAction extends AbstractAction {

    private static final long serialVersionUID = 4083406832930707444L;

    private Document searchInput;
    private DefaultListModel searchResult;
    private PersonService personService;

    public SearchPersonAction(Document searchInput,
                              DefaultListModel searchResult, PersonService personService) {
        this.searchInput = searchInput;
        this.searchResult = searchResult;
        this.personService = personService;
    }

    public void actionPerformed(ActionEvent e) {
        String searchString = getSearchString();

        List<Person> matchedPersons = personService.searchPersons(searchString);

        searchResult.clear();
        for (Person person : matchedPersons) {
            Object elementModel = new PersonElementModel(person);
            searchResult.addElement(elementModel);
        }
    }

    private String getSearchString() {
        try {
            return searchInput.getText(0, searchInput.getLength());
        } catch (BadLocationException e) {
            return null;
        }
    }

}

class PersonElementModel {

    private Person person;

    public PersonElementModel(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return person.getFirstName() + ", " + person.getLastName();
    }
}

interface PersonService {

    List<Person> searchPersons(String searchString);
}

class Person {

    private int id;
    private String firstName;
    private String lastName;

    public Person(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}

class PersonServiceMock implements PersonService {

    private List<Person> personDB;

    public PersonServiceMock() {
        personDB = new ArrayList<Person>();
        personDB.add(new Person(1, "Graham", "Parrish"));
        personDB.add(new Person(2, "Daniel", "Hendrix"));
        personDB.add(new Person(3, "Rachel", "Holman"));
        personDB.add(new Person(4, "Sarah", "Todd"));
        personDB.add(new Person(5, "Talon", "Wolf"));
        personDB.add(new Person(6, "Josephine", "Dunn"));
        personDB.add(new Person(7, "Benjamin", "Hebert"));
        personDB.add(new Person(8, "Lacota", "Browning "));
        personDB.add(new Person(9, "Sydney", "Ayers"));
        personDB.add(new Person(10, "Dustin", "Stephens"));
        personDB.add(new Person(11, "Cara", "Moss"));
        personDB.add(new Person(12, "Teegan", "Dillard"));
        personDB.add(new Person(13, "Dai", "Yates"));
        personDB.add(new Person(14, "Nora", "Garza"));
    }

    public List<Person> searchPersons(String searchString) {
        List<Person> matches = new ArrayList<Person>();

        if (searchString == null) {
            return matches;
        }

        for (Person person : personDB) {
            if (person.getFirstName().contains(searchString)
                    || person.getLastName().contains(searchString)) {
                matches.add(person);
            }

        }
        return matches;
    }
}
