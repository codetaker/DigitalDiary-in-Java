import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.rtf.*;
public class Editor extends JFrame {
    protected JTextPane textpane;
    protected StyleContext context;
    protected DefaultStyledDocument doc;
    protected RTFEditorKit kit;
    protected JFileChooser chooser;
    protected SimpleFilter rtfFilter;
    protected JToolBar toolbar;
    protected JComboBox fonts,Sizes;
    protected SmallToggleButton bold,italic;
    protected String fontName="";
    protected int fontsize=0,start=-1,finish=-1;
    protected boolean SkipUpdate;
    protected SimpleFilter jpgFilter,gifFilter;
    protected ColorMenu foreground,background;
    public Editor(){
        super("Digital Diary");
        setSize(600,400);
        textpane=new JTextPane();
        kit=new RTFEditorKit();
        textpane.setEditorKit(kit);
        context=new StyleContext();
        doc=new DefaultStyledDocument(context);
        textpane.setDocument(doc);
        JScrollPane ps=new JScrollPane(textpane);
        getContentPane().add(ps,BorderLayout.CENTER);
        JMenuBar menubar=createMenuBar();
        setJMenuBar(menubar);
        chooser=new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        rtfFilter=new SimpleFilter("rtf","RTF Documents");
        chooser.setFileFilter(rtfFilter);
        gifFilter=new SimpleFilter("gif","GIF images");
        jpgFilter=new SimpleFilter("jpg","JPG images");
        
        CaretListener lst;
        lst = new CaretListener(){
            public void CaretUpdate(CaretEvent e){
                showAttributes(e.getDot());
            }

            @Override
            public void caretUpdate(CaretEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); 
	  }
        };
        textpane.addCaretListener(lst);
        
        FocusListener flst=new FocusListener(){
            public void focusGained(FocusEvent e){
                if(start>=0 && finish>=0)
                    if(textpane.getCaretPosition()==start){
                        textpane.setCaretPosition(finish);
                        textpane.moveCaretPosition(start);
                    }
                    else textpane.select(start, finish);
            }
            public void focuslost(FocusEvent e){
                start=textpane.getSelectionStart();
                finish=textpane.getSelectionEnd();
            }

            @Override
            public void focusLost(FocusEvent e) {
              
            }
        };
        textpane.addFocusListener(flst);
        
        WindowListener wndCloser = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
      }
    };
      addWindowListener(wndCloser);
      showAttributes(0);
        setVisible(true);
    }
    protected JMenuBar createMenuBar(){
        JMenuBar menubar=new JMenuBar();
        JMenu File=new JMenu("File");
        File.setMnemonic('f');
        ImageIcon icon=new ImageIcon("C:\\Users\\Aagam\\Desktop\\images\\1446207999_import.png");
        Action actionNew=new AbstractAction("New",icon){
            public void actionPerformed(ActionEvent e){
                doc=new DefaultStyledDocument(context);
                textpane.setDocument(doc);
            }
        };
        JMenuItem item=File.add(actionNew);
        item.setMnemonic('n');
        ImageIcon iconOpen = new ImageIcon("C:\\Users\\Aagam\\Desktop\\images\\1446207999_import.png");
        Action actionOpen = new AbstractAction("Open...", iconOpen) {
         public void actionPerformed(ActionEvent e) {
             Editor.this.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
             Thread runner=new Thread(){
                 public void run(){
                     if(chooser.showOpenDialog(Editor.this)!=JFileChooser.APPROVE_OPTION) return;
                     Editor.this.repaint();
                     File choosen=chooser.getSelectedFile();
                     try{
                         InputStream in = new FileInputStream(choosen);
                         doc=new DefaultStyledDocument(context);
                         kit.read(in, doc, 0);
                         textpane.setDocument(doc);
                         in.close();
                     } catch (Exception ex) {ex.printStackTrace();  }
                     Editor.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                 }
             };
             runner.start();
         }
    };
        item=File.add(actionOpen);
        item.setMnemonic('o');
         ImageIcon iconSave = new ImageIcon("C:\\Users\\Aagam\\Desktop\\images\\1446207999_import.png");
         Action actionSave = new AbstractAction("Save...", iconSave) {
              public void actionPerformed(ActionEvent e) {
                  Editor.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                  Thread runner=new Thread(){
                 public void run(){
                     if(chooser.showSaveDialog(Editor.this)!=JFileChooser.APPROVE_OPTION) return;
                     Editor.this.repaint();
                     File choosen=chooser.getSelectedFile();
                     try{
                         OutputStream out = new FileOutputStream(choosen);
                         kit.write(out, doc, 0, doc.getLength());
                         textpane.setDocument(doc);
                         out.close();
                     } catch (Exception ex) {ex.printStackTrace(); }
                     chooser.rescanCurrentDirectory();
                     Editor.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                 }
         };
                  runner.start();
    }     
};
        item=File.add(actionSave);
        item.setMnemonic('s');
        File.addSeparator();
         Action actionExit = new AbstractAction("Exit") {
      public void actionPerformed(ActionEvent e) {  System.exit(0);}
};
    item =  File.add(actionExit); 
    item.setMnemonic('x');
    menubar.add(File);
    toolbar=new JToolBar();
    JButton bNew=new SmallButton(actionNew,"New document");
    toolbar.add(bNew);
    JButton bOpen=new SmallButton(actionOpen,"Open RTF document");
    toolbar.add(bOpen);
    JButton bSave=new SmallButton(actionSave,"Save RTF document");
    toolbar.add(bSave);
    getContentPane().add(toolbar,BorderLayout.NORTH);

    GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontNames=ge.getAvailableFontFamilyNames();
    toolbar.addSeparator();
    fonts=new JComboBox(fontNames);
    fonts.setMaximumSize(fonts.getPreferredSize());
    fonts.setEditable(true);
    ActionListener lst = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          fontName=fonts.getSelectedItem().toString();
          MutableAttributeSet attr=new SimpleAttributeSet();
          StyleConstants.setFontFamily(attr, fontName);
          setAttributeSet(attr);
          textpane.grabFocus();
      }
    };
    fonts.addActionListener(lst);
    toolbar.add(fonts);
    toolbar.addSeparator();
    Sizes=new JComboBox(new String[]{"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"});
    Sizes.setMaximumSize(Sizes.getPreferredSize());
    Sizes.setEditable(true);
    lst=new ActionListener(){
     public void actionPerformed(ActionEvent e) {
        int m_fontSize = 0;
        try {
          m_fontSize = Integer.parseInt(Sizes.getSelectedItem().toString());
        }
        catch (NumberFormatException ex) { return; }
        fontsize = m_fontSize;
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontSize(attr, m_fontSize);
        setAttributeSet(attr);
        textpane.grabFocus();
      }
    };
        Sizes.addActionListener(lst);
        toolbar.add(Sizes);
        toolbar.addSeparator();
        ImageIcon img1=new ImageIcon("C:\\Users\\Aagam\\Desktop\\images\\text_bold");
        ImageIcon img2=new ImageIcon("C:\\Users\\Aagam\\Desktop\\images\\text_bold");
        bold=new SmallToggleButton(false,img1,img2,"BoldFont");
        lst = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, bold.isSelected());
        setAttributeSet(attr);
        textpane.grabFocus();
      }
    };
        bold.addActionListener(lst);
        toolbar.add(bold);
        img1=new ImageIcon("C:\\Users\\Aagam\\Desktop\\images\\text_italic");
        img2=new ImageIcon("C:\\Users\\Aagam\\Desktop\\images\\text_italic");
        italic=new SmallToggleButton(false,img1,img2,"Italic Font");
        lst = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setItalic(attr, italic.isSelected());
        setAttributeSet(attr);
        textpane.grabFocus();
      }
    };
        italic.addActionListener(lst);
        toolbar.add(italic);
        //Upto 20.2
        
        JMenu mFormat=new JMenu("Format");
        mFormat.setMnemonic('o');
        foreground=new ColorMenu("Foreground");
        foreground.setColor(textpane.getForeground());
        foreground.setMnemonic('f');
        lst=new ActionListener(){
            public void actionPerformed(ActionEvent e){
                MutableAttributeSet attr=new SimpleAttributeSet();
                StyleConstants.setForeground(attr, foreground.getColor());
                setAttributeSet(attr);
            }
        };
        foreground.addActionListener(lst);
        mFormat.add(foreground);
        MenuListener ml=new MenuListener(){
         public void menuSelected(MenuEvent e) {
        int p = textpane.getCaretPosition();
        AttributeSet a = doc.getCharacterElement(p).
        getAttributes();
        Color c = StyleConstants.getForeground(a);
        foreground.setColor(c);
      }
     public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    };
        foreground.addMenuListener(ml);
        
        background=new ColorMenu("Background");
        background.setColor(textpane.getBackground());
        background.setMnemonic('b');
         lst = new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBackground(attr, background.getColor());
        setAttributeSet(attr);
      }
    };
         background.addActionListener(lst);
         mFormat.add(background);
           ml = new MenuListener() {
      public void menuSelected(MenuEvent e) {
        int p = textpane.getCaretPosition();
        AttributeSet a = doc.getCharacterElement(p).
          getAttributes();
        Color c = StyleConstants.getBackground(a);
        background.setColor(c);
      }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    };
           background.addMenuListener(ml);
           mFormat.addSeparator();
           
       item=new JMenuItem("Insert Image");
       item.setMnemonic('i');
  
              item.addActionListener(lst);
              mFormat.add(item);
              menubar.add(mFormat);
        getContentPane().add(toolbar, BorderLayout.NORTH);     
        return menubar;
   }
    
    protected void showAttributes(int p){
        SkipUpdate=true;
        AttributeSet a=doc.getCharacterElement(p).getAttributes();
        String name=StyleConstants.getFontFamily(a);
        if(!fontName.equals(name)){
            fontName=name;
            fonts.setSelectedItem(name);
        }
        int size=StyleConstants.getFontSize(a);
        if(fontsize!=size){
            fontsize=size;
            Sizes.setSelectedItem(Integer.toString(fontsize));
        }
        boolean Bold=StyleConstants.isBold(a);
         if (Bold != bold.isSelected())
      bold.setSelected(Bold);
    boolean Italic = StyleConstants.isItalic(a);
    if (Italic != italic.isSelected())
      italic.setSelected(Italic);
        SkipUpdate = false;
  }
    
    
protected void setAttributeSet(AttributeSet attr) {
    if (SkipUpdate)  return;
    int xStart = textpane.getSelectionStart();
    int xFinish = textpane.getSelectionEnd();
    if (!textpane.hasFocus()) {
      xStart = start;
      xFinish = finish;
    }
    if (xStart != xFinish) doc.setCharacterAttributes(xStart, xFinish - xStart,  attr, false);
    else {
    MutableAttributeSet inputAttributes =kit.getInputAttributes();
    inputAttributes.addAttributes(attr);
    }
}
   
}


class SmallButton extends JButton implements MouseListener{
  protected Border m_raised;
  protected Border m_lowered;
  protected Border m_inactive;
  public SmallButton(Action act, String tip) {
    super((Icon)act.getValue(Action.SMALL_ICON));
    m_raised = new BevelBorder(BevelBorder.RAISED);
    m_lowered = new BevelBorder(BevelBorder.LOWERED);
    m_inactive = new EmptyBorder(2, 2, 2, 2);
    setBorder(m_inactive);
    setMargin(new Insets(1,1,1,1));
    setToolTipText(tip);
    addActionListener(act);
    addMouseListener(this);
    setRequestFocusEnabled(false);
  }

  public float getAlignmentY() { return 0.5f; }
  public void mousePressed(MouseEvent e) {
    setBorder(m_lowered);
  }
  public void mouseReleased(MouseEvent e) {
    setBorder(m_inactive);
  }
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {
    setBorder(m_raised);
  }
  public void mouseExited(MouseEvent e) {
    setBorder(m_inactive);
  }
}

 class SimpleFilter extends FileFilter{
    private String m_description = null;
    private String m_extension = null;

    public SimpleFilter(String extension, String description) {
      m_description = description;
      m_extension = "." + extension.toLowerCase();
    }

    public String getDescription() {
      return m_description;
    }

    public boolean accept(File f) {
      if (f == null) return false;
      if (f.isDirectory()) return true;
      return f.getName().toLowerCase().endsWith(m_extension);
    }
  }


class SmallToggleButton extends JToggleButton
  implements ItemListener
{
  protected Border m_raised;
  protected Border m_lowered;

  public SmallToggleButton(boolean selected,
   ImageIcon imgUnselected, ImageIcon imgSelected, String tip) {
    super(imgUnselected, selected);
    setHorizontalAlignment(CENTER);
    setBorderPainted(true);

    m_raised = new BevelBorder(BevelBorder.RAISED);
    m_lowered = new BevelBorder(BevelBorder.LOWERED);
    setBorder(selected ? m_lowered : m_raised);
    setMargin(new Insets(1,1,1,1));
    setToolTipText(tip);
    setRequestFocusEnabled(false);
    setSelectedIcon(imgSelected);
    addItemListener(this);
  }

  public float getAlignmentY() { return 0.5f; }

  public void itemStateChanged(ItemEvent e) {
    setBorder(isSelected() ? m_lowered : m_raised);
  }
}

class ColorMenu extends JMenu
{

  protected Border m_unselectedBorder;
  protected Border m_selectedBorder;
  protected Border m_activeBorder;
  protected Hashtable m_panes;
  protected ColorPane m_selected;

  public ColorMenu(String name) {
    super(name);

    m_unselectedBorder = new CompoundBorder(
      new MatteBorder(1, 1, 1, 1, getBackground()),
      new BevelBorder(BevelBorder.LOWERED,
      Color.white, Color.gray));

    m_selectedBorder = new CompoundBorder(
      new MatteBorder(2, 2, 2, 2, Color.red),
      new MatteBorder(1, 1, 1, 1, getBackground()));

    m_activeBorder = new CompoundBorder(
      new MatteBorder(2, 2, 2, 2, Color.blue),
      new MatteBorder(1, 1, 1, 1, getBackground()));

    JPanel p = new JPanel();

    p.setBorder(new EmptyBorder(5, 5, 5, 5));
    p.setLayout(new GridLayout(8, 8));
    m_panes = new Hashtable();

    int[] values = new int[] { 0, 128, 192, 255 };
    for (int r=0; r<values.length; r++) {
      for (int g=0; g<values.length; g++) {
        for (int b=0; b<values.length; b++) {
          Color c = new Color(values[r], values[g], values[b]);
          ColorPane pn = new ColorPane(c);
          p.add(pn);
          m_panes.put(c, pn);

        }
      }
    }
    add(p);
  }

  public void setColor(Color c) {
    Object obj = m_panes.get(c);

    if (obj == null)
      return;

    if (m_selected != null)
      m_selected.setSelected(false);

    m_selected = (ColorPane)obj;
    m_selected.setSelected(true);

  }

  public Color getColor() {

    if (m_selected == null)
     return null;

    return m_selected.getColor();
  }

  public void doSelection() {
    fireActionPerformed(new ActionEvent(this,
      ActionEvent.ACTION_PERFORMED, getActionCommand()));
  }

  class ColorPane extends JPanel implements MouseListener
  {
    protected Color m_c;
    protected boolean m_selected;

    public ColorPane(Color c) {
      m_c = c;
      setBackground(c);
      setBorder(m_unselectedBorder);

      String msg = "R "+c.getRed()+", G "+c.getGreen()+
        ", B "+c.getBlue();
      
	setToolTipText(msg);
      addMouseListener(this);

    }

    public Color getColor() { return m_c; }
    public Dimension getPreferredSize() {
      return new Dimension(15, 15);

    }

    public Dimension getMaximumSize() { return getPreferredSize(); }
    public Dimension getMinimumSize() { return getPreferredSize(); }

    public void setSelected(boolean selected) {
      m_selected = selected;

      if (m_selected)
        setBorder(m_selectedBorder);
      else
        setBorder(m_unselectedBorder);
    }

    public boolean isSelected() { return m_selected; }
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {

      setColor(m_c);
      MenuSelectionManager.defaultManager().clearSelectedPath();
      doSelection();
    }

    public void mouseEntered(MouseEvent e) {
      setBorder(m_activeBorder);
    }

    public void mouseExited(MouseEvent e) {
      setBorder(m_selected ? m_selectedBorder :
        m_unselectedBorder);

    }

  }

}