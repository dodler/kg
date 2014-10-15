/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import haulmaunt.lyan.ui.markupexception.InvalidMarkupException;
import haulmaunt.lyan.ui.markupexception.MissingTableModelException;
import haulmaunt.lyan.ui.markupexception.MissingMouseListenerException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Артем
 */
public class MarkupLoader {

    private static final MarkupLoader markupLoaderInstance = new MarkupLoader();

    public static MarkupLoader getMarkupLoaderInstance() {
        return markupLoaderInstance;
    }
    public HashMap<String, MouseListener> mouseListeners; // пока не буду заморачиваться с инкапсуяцией
    public HashMap<String, Class> dialogClasses; // классы диалогов
    public HashMap<String, Class> tableModels; // модели таблиц
    public HashMap<String, Class> frameClasses; // классы фреймов, правда кажется нужен только один класс фрейма
    // но ладно
    public HashMap<String, Component> components; // все объекты 
    public HashMap<String, Class> tableClasses; // классы таблиц
    public HashMap<String, Class> labelClasses;

    /**
     * класс фрейма должен быть конструктор без параметров
     *
     * @param name
     * @param classInstance
     */
    public void addFrameClass(String name, Class classInstance) {
        frameClasses.put(name, classInstance);
    }

    public void addFrameClass(Class classInstance) {
        frameClasses.put(classInstance.getSimpleName(), classInstance);
    }

    public void removeFrameClass(String name) {
        frameClasses.remove(name);
    }

    /**
     * модель таблицы для таблицы обхявленной в разметке обязательно должен быть
     * конструктор без параметров
     *
     * @param name
     * @param classInstance - класс модели
     */
    public void addTableModel(String name, Class classInstance) {
        tableModels.put(name, classInstance);
    }

    public void addTableModel(Class classInstance) {
        tableModels.put(classInstance.getSimpleName(), classInstance);
    }

    public void removeTableModel(String name) {
        tableModels.remove(name);
    }

    /**
     * класс для объекта объявленного в xml разметке класс должен обязательно
     * иметь конструктор без параметров
     *
     * @param name - имя класса
     * @param classInstance - класс
     */
    public void addDialogClass(String name, Class classInstance) {
        dialogClasses.put(name, classInstance);
    }

    /**
     *
     * @param classInstance
     */
    public void addDialogClass(Class classInstance) {
        dialogClasses.put(classInstance.getSimpleName(), classInstance);
    }

    public void removeDialogClass(String name) {
        dialogClasses.remove(name);
    }

    /**
     * метод добавляет слушатель событий мыши в колелкцию имя - имя класса
     * вместе с пакетом
     *
     * @param ml - слушатель событий
     */
    public void addMouseListenerInstance(MouseListener ml) {
        mouseListeners.put(ml.getClass().getSimpleName(), ml);
    }

    /**
     * метод добавляет слушатель событий мыши в коллекцию с заданным именем
     *
     * @param name - имя слушателя событий
     * @param ml - слушатель событий
     */
    public void addMouseListenerInstanceByName(String name, MouseListener ml) {
        mouseListeners.put(name, ml);
    }

    public void removeMouseListenerInstance(String name) {
        mouseListeners.remove(name);
    }

    /**
     * возвращает визуальный компонент, указанный в xml разметке
     *
     * @param name - имя компонента
     * @return Component - ссылку на визуальный объект
     */
    public Component getComponentByName(String name) {
        return components.get(name);
    }

    private MarkupLoader() {
        mouseListeners = new HashMap<>();
        components = new HashMap<>();
        dialogClasses = new HashMap<>();
        tableModels = new HashMap<>();
        tableClasses = new HashMap<>();
        labelClasses = new HashMap<>();
        frameClasses = new HashMap<>();
    }
    private Document doc;

    /**
     * запускает инициализацию разметки, загруженной из xml файла путь до xml
     * файла надо подавать в качестве аргумента
     *
     * @param fileName - полный путь до файла разметки
     * @param parent - родительский контейнер. TODO если указан null сделать
     * новое окно
     * @throws SAXException - исключение парсера xml
     * @throws IOException - ошибка считывания файла разметки
     * @throws ParserConfigurationException - ошибка парсера
     * @throws MissingMouseListenerException - ошибка в формате или коде - не
     * указан слушатель событий мыши для объекта
     * @throws Exception - не помню где и как но важно
     */
    public void loadMarkup(String fileName, Container parent) throws SAXException, IOException, ParserConfigurationException, MissingMouseListenerException, Exception {
        File markupSource = new File(fileName); // загрузка разметки
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(markupSource);
        initMarkup(parent, doc);
    }

    /**
     * метод позволяет получить компонент из разметки возвращает общий тип
     * Component по строковому ключу - имени
     *
     * @param name - имя компоненента, который нужно получить, должен быть
     * прописан в разметке - тег name
     * @return - визуальный компонент с именем
     * @throws NoSuchElementException - если данный компонент с заданным именем
     * отсутствует, или имя компонента направильное
     */
    public Component getComponent(String name) throws NoSuchElementException {
        if (components.containsKey(name)) {
            return components.get(name); // если компонент есть в разметке
        } else {
            throw new NoSuchElementException();  // если его нету
        }
    }

    /**
     * метод загрузки визуальных компонентов из файла xml поддерживает внешние
     * классы элементов, слушатели событий, пока работает все что есть, но есть
     * не все что надо нужно добавит другие элементы gui и сделать другие
     * обработчики события, дописать комментарии
     *
     * @param parent - родительский контейнер
     * @param node - узел xmlб содержащий инфорамацию об элементах
     * @throws MissingTableModelException - отсутствует модель для таблицы
     * @throws MissingMouseListenerException - отсутствует слушатель событий для
     * мыши
     * @throws Exception - что то отсутствует, надо выяснить что и заменить
     * исключение
     */
    private void initMarkup(Component parent, Node node) throws
            MissingTableModelException,
            MissingMouseListenerException,
            InvalidMarkupException,
            Exception {
        NodeList currentComponents = node.getChildNodes();
        Component t;
        NamedNodeMap attributes;
        String className, name, label, backgroundColor, focusable, fontName, fontSize, fontStyle;
        // backgroundColour - setBackground, focusable - setFocusable, font - setFont, 
        for (int i = 0; i < currentComponents.getLength(); i++) { // проходим по всем layout
            attributes = currentComponents.item(i).getAttributes();

            if (attributes == null) {
                continue; // если атрибутов нет то смысла продолджать нету
            }

            if (attributes.getNamedItem("fontStyle") != null) { // получаем имя класса
                fontStyle = attributes.getNamedItem("fontStyle").getTextContent();
            } else {
                fontStyle = "";
            }

            if (attributes.getNamedItem("fontSize") != null) { // получаем имя класса
                fontSize = attributes.getNamedItem("fontSize").getTextContent();
            } else {
                fontSize = "";
            }

            if (attributes.getNamedItem("fontName") != null) { // получаем имя класса
                fontName = attributes.getNamedItem("fontName").getTextContent();
            } else {
                fontName = "";
            }

            if (attributes.getNamedItem("focusable") != null) { // значение свойства focusable
                focusable = attributes.getNamedItem("focusable").getTextContent();
            } else {
                focusable = "";
            }

            if (attributes.getNamedItem("backgroundColour") != null) { // получаем цвет фона
                backgroundColor = attributes.getNamedItem("backgroundColour").getTextContent();
            } else {
                backgroundColor = "";
            }


            if (attributes.getNamedItem("class") != null) { // получаем имя класса
                className = attributes.getNamedItem("class").getTextContent();
            } else {
                className = "";
            }

            if (attributes.getNamedItem("name") != null) {// имя компонента
                name = attributes.getNamedItem("name").getTextContent();
            } else {
                name = "";
            }

            if (attributes.getNamedItem("label") != null) { // возможная надпись
                label = attributes.getNamedItem("label").getTextContent();
            } else {
                label = "";
            }

            switch (currentComponents.item(i).getNodeName()) {

                case "menuBar":

                    /*if (!parent.getClass().equals(JFrame.class)){
                     throw new InvalidMarkupException();
                     }*/

                    if ((className.equals("JMenuBar") || className.equals("")) && !frameClasses.containsKey(className)) {
                        t = new JMenuBar();
                    } else {
                        t = (JMenuBar) labelClasses.get(className).getConstructor().newInstance();
                    }

                    ((Container) parent).add(t);
                    initMarkup(t, currentComponents.item(i));
                    ((JFrame) parent).setJMenuBar((JMenuBar) t);

                    components.put(attributes.getNamedItem("name").getTextContent(), t);
                    break;

                case "menuItem":

                    if (!parent.getClass().equals(JMenu.class)) {
                        throw new InvalidMarkupException(); // произошла ошибка разметки
                        // имеется в виду что родительский элемент не jmenu
                    }

                    if ((className.equals("JMenuItem") || className.equals("")) && !frameClasses.containsKey(className)) {
                        t = new JMenuItem();
                    } else {
                        t = (JMenuItem) labelClasses.get(className).getConstructor().newInstance();
                    }

                    String mouseListenerName = "";

                    if (attributes.getNamedItem("mouseListener") != null) {
                        mouseListenerName = attributes.getNamedItem("mouseListener").getTextContent();
                    }

                    if (!mouseListenerName.equals("") && mouseListeners.containsKey(mouseListenerName)) {
                        ((JMenuItem) t).addMouseListener(mouseListeners.get(mouseListenerName));
                    } else if (!mouseListenerName.equals("")) {
                    }

                    if (!label.equals("")) {
                        ((JMenuItem) t).setText(label);
                    } else {
                        throw new InvalidMarkupException();
                    }

                    ((JMenu) parent).add((JMenuItem) t);
                    components.put(attributes.getNamedItem("name").getTextContent(), t);
                    break;

                case "menu":

                    if (!parent.getClass().equals(JMenuBar.class)) {
                        throw new InvalidMarkupException(); // ошибка разметки
                    }

                    if ((className.equals("JMenu") || className.equals("")) && !frameClasses.containsKey(className)) {
                        t = new JMenu();
                    } else {
                        t = (JMenu) labelClasses.get(className).getConstructor().newInstance();
                    }

                    if (!label.equals("")) {
                        ((JMenu) t).setText(label);
                    }

                    initMarkup(t, currentComponents.item(i));

                    ((JMenuBar) parent).add(t);

                    break;

                case "label":

                    if ((className.equals("JLabel") || className.equals("")) && !frameClasses.containsKey(className)) {
                        t = new JLabel(label);
                    } else {
                        t = (JLabel) labelClasses.get(className).getConstructor(String.class).newInstance(label);
                    }
                    t.setBounds(
                            getAttributeValue("x", attributes),
                            getAttributeValue("y", attributes),
                            getAttributeValue("width", attributes),
                            getAttributeValue("height", attributes));
                    ((Container) parent).add(t);
                    components.put(attributes.getNamedItem("name").getTextContent(), t);
                    break;

                case "text":

                    if ((className.equals("JTextField") || className.equals("")) && !frameClasses.containsKey(className)) {
                        t = new JTextField(label);
                    } else {
                        t = (JTextField) labelClasses.get(className).getConstructor().newInstance();
                    }
                    t.setBounds(
                            getAttributeValue("x", attributes),
                            getAttributeValue("y", attributes),
                            getAttributeValue("width", attributes),
                            getAttributeValue("height", attributes));
                    ((Container) parent).add(t);
                    components.put(attributes.getNamedItem("name").getTextContent(), t);

                    break;

                case "frame":
                    // тут можно создать родительское окно
                    // не до конца еще знаю gui api java поэтому считаю что можно создать несколько фреймов
                    if ((className.equals("JFrame") || className.equals("")) && !frameClasses.containsKey(className)) {
                        t = new JFrame(label); // выбирваем класс для создания фрейма
                    } else { // либо стандартный
                        t = (JFrame) frameClasses.get(className).getConstructor().newInstance(); // либо заданный
                    }
                    t.setBounds( // и габариты
                            getAttributeValue("x", attributes),
                            getAttributeValue("y", attributes),
                            getAttributeValue("width", attributes),
                            getAttributeValue("height", attributes));

                    initMarkup((JFrame) t, currentComponents.item(i)); // элемент может содрежать дочерние элементы, поэтому запускаем рекурсивную обработку "детей"
                    boolean enable = Boolean.valueOf(attributes.getNamedItem("autoEnable").getTextContent()); // почему то никак не задействовано
                    String layout = attributes.getNamedItem("layout").getTextContent();
                    switch (layout) { // TODO добавьт другие layout
                        case "GridLayout":
                            ((JFrame) t).setLayout(new GridLayout());
                            break;
                        case "FlowLayout":
                            ((JFrame) t).setLayout(new FlowLayout());
                            break;
                        case "BorderLayout":
                            ((JFrame) t).setLayout(new BorderLayout());
                            break;
                        default:
                            ((JFrame) t).setLayout(null); // выбираем тип разметки
                    }
                    if (enable)
                    ((JFrame) t).setVisible(enable); // можно сразу сделать окно доступным

                    components.put(attributes.getNamedItem("name").getTextContent(), t);

                    break;
                case "button":
                    if ((className.equals("") || className.equals("JButton")) && !dialogClasses.containsKey(className)) {
                        t = new JButton(label);
                    } else {
                        t = (JButton) dialogClasses.get(className).getConstructor(String.class).newInstance(label);
                    }
                    mouseListenerName = attributes.getNamedItem("mouseListener").getTextContent();
                    if (!mouseListenerName.equals("") && mouseListeners.containsKey(mouseListenerName)) { // ищем нужный обработчик
                        t.addMouseListener(mouseListeners.get(mouseListenerName)); // если есть назначаем
                    }
                    t.setBounds(
                            getAttributeValue("x", attributes),
                            getAttributeValue("y", attributes),
                            getAttributeValue("width", attributes),
                            getAttributeValue("height", attributes));

                    ((Container) parent).add(t);
                    components.put(attributes.getNamedItem("name").getTextContent(), t);


                    String iconUrl;// адрес иконки
                    if (attributes.getNamedItem("icon") != null) {
                        iconUrl = attributes.getNamedItem("icon").getTextContent();
                    } else {
                        continue;
                    }

                    ImageIcon ii; // иконка для кнопки
                    if (!iconUrl.equals("")) {
                        ii = new ImageIcon(iconUrl);
                    } else {
                        continue;
                    }
                    ((JButton) t).setIcon(ii);

                    break;
                case "dialog":

                    if (className.equals("JDialog") && !dialogClasses.containsKey(className)) {
                        t = new JDialog((JFrame) parent, label);
                    } else {
                        t = (JDialog) dialogClasses.get(className).getConstructor().newInstance();
                    }
                    t.setBounds(
                            getAttributeValue("x", attributes),
                            getAttributeValue("y", attributes),
                            getAttributeValue("width", attributes),
                            getAttributeValue("height", attributes));

                    initMarkup((JDialog) t, currentComponents.item(i));
                    enable = Boolean.valueOf(attributes.getNamedItem("autoEnable").getTextContent());
                    layout = attributes.getNamedItem("layout").getTextContent();
                    switch (layout) { // TODO добавьт другие layout
                        default:
                            ((JDialog) t).setLayout(null); // выбираем тип разметки
                    }
                    ((JDialog) t).setVisible(enable);

                    components.put(attributes.getNamedItem("name").getTextContent(), t);

                    break;
                case "table": // обработка таблиц

                    enable = Boolean.valueOf(attributes.getNamedItem("useScroll").getTextContent()); // проверяем есть ли тег скроллинга

                    JTable t1; // временный объект, если надо задействовать scrollpane

                    if ((className.equals("") || className.equals("JTable")) && !dialogClasses.containsKey(className)) { // тут идет инициализация таблицы
                        t1 = new JTable(); // либо стандартным классом
                    } else {
                        t1 = (JTable) dialogClasses.get(className).getConstructor().newInstance(); // либо указанным
                    }

                    if (tableModels.containsKey(attributes.getNamedItem("model").getTextContent())) { // назначаем модель данных
                        ((JTable) t1).setModel((AbstractTableModel) tableModels.get(attributes.getNamedItem("model").getTextContent()).newInstance());
                    } else { // если она не была задана 
                        throw new MissingTableModelException(); // то выбрасываем исключение
                    }

                    if (enable) {
                        t = new JScrollPane((JTable) t1); // если нужно добавляем в скрол пейн
                    } else {
                        t = t1; // иначе просто обновляем стандартную ссылку
                    }

                    t.setBounds(
                            getAttributeValue("x", attributes),
                            getAttributeValue("y", attributes),
                            getAttributeValue("width", attributes),
                            getAttributeValue("height", attributes)); // координаты
                    ((Container) parent).add(t); // добавляем в список отображения родителя
                    components.put(name, t); // добавляем в список объектов

                    break;
                default:
                    continue;
            }
            if (t != null) {
                if (!fontName.equals("")) {
                    t.setFont(new Font(fontName, Integer.parseInt(fontStyle), Integer.parseInt(fontSize))); // ставим новый шрифт
                }
                if (!backgroundColor.equals("")) { // ставим цвет фона
                    t.setBackground(new Color(Integer.parseInt(backgroundColor)));
                }
                if (!focusable.equals("")) { // и выставляем свойство focusable
                    t.setFocusable(Boolean.parseBoolean(focusable));
                }
                if (!name.equals("")) {
                    t.setName(name); // это свойство общее для всех компонентов
                }
            }
            //
            if (parent != null) {
                parent.repaint();
            }
        }
    }

    private int getAttributeValue(String key, NamedNodeMap attr) {
        if (attr.getNamedItem(key) == null) {
            return 0;
        }
        return Integer.parseInt(attr.getNamedItem(key).getTextContent());
    }
}
