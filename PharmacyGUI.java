import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PharmacyGUI extends JFrame {

    static final Color BG        = new Color(0x080E1A);
    static final Color PANEL     = new Color(0x0F1825);
    static final Color CARD      = new Color(0x111A28);
    static final Color CARD2     = new Color(0x172035);
    static final Color BORDER    = new Color(0x1E2D45);
    static final Color GOLD      = new Color(0xC9A84C);
    static final Color GOLD2     = new Color(0xFFD060);
    static final Color GOLD_DIM  = new Color(0x6A5818);
    static final Color TEXT      = new Color(0xE8EEFF);
    static final Color TEXT_DIM  = new Color(0x5A6A8A);
    static final Color TEXT_MID  = new Color(0x8A9BBF);
    static final Color GREEN     = new Color(0x00C896);
    static final Color RED       = new Color(0xFF3D55);
    static final Color BLUE      = new Color(0x3A84FF);
    static final Color AMBER     = new Color(0xFFAA00);
    static final Color PURPLE    = new Color(0xA040F0);
    static final Color CYAN      = new Color(0x00B8D4);
    static final Color TEAL      = new Color(0x00BFA5);

    static final Font F_LOGO  = new Font("Georgia",   Font.BOLD,  30);
    static final Font F_TITLE = new Font("Georgia",   Font.BOLD,  22);
    static final Font F_HEAD  = new Font("Georgia",   Font.BOLD,  16);
    static final Font F_SUB   = new Font("Segoe UI",  Font.BOLD,  13);
    static final Font F_BODY  = new Font("Segoe UI",  Font.PLAIN, 13);
    static final Font F_SMALL = new Font("Segoe UI",  Font.PLAIN, 11);
    static final Font F_MONO  = new Font("Consolas",  Font.PLAIN, 12);
    static final Font F_BTN   = new Font("Segoe UI",  Font.BOLD,  12);
    static final Font F_BADGE = new Font("Segoe UI",  Font.BOLD,  10);
    static final Font F_STAT  = new Font("Georgia",   Font.BOLD,  40);

    private User        currentUser  = null;
    private Sales       sales;
    private AlertSystem alertSystem;
    private CardLayout  rootLayout;
    private JPanel      rootPanel, contentArea;
    private CardLayout  contentLayout;
    private int         activeSidebarIndex = 0;
    private final JButton[] sidebarBtns = new JButton[8];
    private int         sidebarIdx = 0;
    private JLabel      clockLabel, userLabel;
    private JLabel      statMeds, statAlerts, statRevenue, statLow;
    private DefaultTableModel inventoryModel;
    private JTable      inventoryTable;
    private JTextField  liveSearch;
    private JTextArea   billArea, salesLogArea, reportsArea, alertsArea, actLogArea;
    private Timer       toastTimer;
    private JLabel      statusMeds, statusTime, statusUser;

    public static void launchGUI() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new PharmacyGUI().setVisible(true));
    }

    public PharmacyGUI() {
        alertSystem = new AlertSystem(30);
        sales = new Sales();
        setTitle("Pharmacy Management System");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1400, 860);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { confirmExit(); }
        });
        rootLayout = new CardLayout();
        rootPanel  = new JPanel(rootLayout);
        rootPanel.setBackground(BG);
        rootPanel.add(buildLogin(), "LOGIN");
        rootPanel.add(buildApp(),   "APP");
        add(rootPanel);
        startClock();
        rootLayout.show(rootPanel, "LOGIN");
    }

    private JPanel buildLogin() {
        JPanel root = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx=getWidth()/2;
                g2.setPaint(new RadialGradientPaint(cx, getHeight()/3, Math.max(cx,getHeight()/2)*1.2f,
                    new float[]{0f,0.5f,1f},
                    new Color[]{new Color(0x0E2040),new Color(0x080E1A),new Color(0x020508)}));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setStroke(new BasicStroke(0.5f));
                g2.setColor(new Color(30,45,70,55));
                for(int x=0;x<getWidth();x+=70) g2.drawLine(x,0,x,getHeight());
                for(int y=0;y<getHeight();y+=70) g2.drawLine(0,y,getWidth(),y);
                g2.setPaint(new RadialGradientPaint(cx,0,getWidth()*0.65f,
                    new float[]{0f,1f},new Color[]{new Color(201,168,76,55),new Color(201,168,76,0)}));
                g2.fillRect(0,0,getWidth(),getHeight()/2);
                g2.dispose();
            }
        };
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,new Color(0x131D2E),0,getHeight(),new Color(0x0A1120)));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),18,18);
                g2.setColor(BORDER); g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,18,18);
                g2.setColor(GOLD); g2.setStroke(new BasicStroke(2f));
                g2.drawLine(50,0,getWidth()-50,0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(44,54,44,54));
        card.setPreferredSize(new Dimension(460,560));

        JLabel plus=lbl("+",new Font("Georgia",Font.BOLD,38),GOLD); plus.setAlignmentX(CENTER_ALIGNMENT);
        JLabel pharma=lbl("PHARMA",F_LOGO,GOLD); pharma.setAlignmentX(CENTER_ALIGNMENT);
        JLabel sys=lbl("Management System",new Font("Georgia",Font.ITALIC,13),TEXT_MID); sys.setAlignmentX(CENTER_ALIGNMENT);
        JLabel inst=lbl("Institute of Business Management  |  OOP Lab",F_SMALL,TEXT_DIM); inst.setAlignmentX(CENTER_ALIGNMENT);

        JPanel div=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                int w=getWidth();
                g2.setPaint(new GradientPaint(0,0,new Color(201,168,76,0),w/2,0,GOLD));
                g2.fillRect(0,2,w/2,1);
                g2.setPaint(new GradientPaint(w/2,0,GOLD,w,0,new Color(201,168,76,0)));
                g2.fillRect(w/2,2,w/2,1);
                g2.dispose();
            }
        };
        div.setOpaque(false); div.setPreferredSize(new Dimension(340,6));
        div.setMaximumSize(new Dimension(340,6)); div.setAlignmentX(CENTER_ALIGNMENT);

        JLabel signLbl=lbl("Sign In to Continue",F_SUB,GOLD); signLbl.setAlignmentX(CENTER_ALIGNMENT);
        JTextField uField=loginField("Username");
        JPasswordField pField=new JPasswordField(); stylePass(pField);
        JLabel errLbl=lbl(" ",F_SMALL,RED); errLbl.setAlignmentX(CENTER_ALIGNMENT);
        JButton loginBtn=goldBtn("SIGN IN"); loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(352,46));
        JLabel hint=lbl("Authorized Personnel Only",new Font("Segoe UI",Font.ITALIC,11),new Color(0x283848));
        hint.setAlignmentX(CENTER_ALIGNMENT);
        clockLabel=lbl("",F_SMALL,TEXT_DIM); clockLabel.setAlignmentX(CENTER_ALIGNMENT);

        card.add(plus); card.add(Box.createVerticalStrut(2));
        card.add(pharma); card.add(Box.createVerticalStrut(2));
        card.add(sys); card.add(Box.createVerticalStrut(4));
        card.add(inst); card.add(Box.createVerticalStrut(16));
        card.add(div); card.add(Box.createVerticalStrut(14));
        card.add(signLbl); card.add(Box.createVerticalStrut(22));
        card.add(uField); card.add(Box.createVerticalStrut(10));
        card.add(pField); card.add(Box.createVerticalStrut(8));
        card.add(errLbl); card.add(Box.createVerticalStrut(6));
        card.add(loginBtn); card.add(Box.createVerticalStrut(16));
        card.add(hint); card.add(Box.createVerticalStrut(8));
        card.add(clockLabel);
        root.add(card);

        final int[] attempts={0};
        ActionListener doLogin=e->{
            String u=uField.getText().trim();
            String p=new String(pField.getPassword()).trim();
            User[] users={
                new Admin("ahmed","12345"),new Admin("ali","12345"),
                new Admin("zainab","12345"),new Admin("laiba","12345"),
                new Admin("haseeb","12345"),new Staff("staff1","123"),new Staff("staff2","123")
            };
            User found=null;
            for(User usr:users) if(usr.getUsername().equals(u)&&usr.getPassword().equals(p)){found=usr;break;}
            if(found!=null){
                currentUser=found;
                userLabel.setText(fmtName(currentUser.getUsername())+"  |  "+currentUser.getRole());
                rootLayout.show(rootPanel,"APP"); refreshDashboard();
                alertSystem.checkAllOnStartup();
                if(alertSystem.getAlertCount()>0) showAlertDialog(alertSystem.getAlertHistory());
                else showToast("Welcome, "+fmtName(currentUser.getUsername())+"!",GREEN);
                attempts[0]=0; uField.setText(""); pField.setText(""); errLbl.setText(" ");
            } else {
                attempts[0]++; int rem=3-attempts[0];
                if(rem<=0){errLbl.setText("Account locked.");loginBtn.setEnabled(false);}
                else errLbl.setText("Incorrect credentials.  "+rem+" attempt(s) left.");
            }
        };
        loginBtn.addActionListener(doLogin);
        pField.addActionListener(doLogin);
        uField.addActionListener(ev->pField.requestFocus());
        return root;
    }

    private String fmtName(String u){
        switch(u){case"ahmed":return"Ahmed";case"ali":return"Ali";case"zainab":return"Zainab";
            case"laiba":return"Laiba";case"haseeb":return"Haseeb";
            default:return u.substring(0,1).toUpperCase()+u.substring(1);}
    }

    private JPanel buildApp() {
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(BG);
        root.add(buildTopBar(),  BorderLayout.NORTH);
        root.add(buildSidebar(),BorderLayout.WEST);
        root.add(buildContent(),BorderLayout.CENTER);
        root.add(buildStatusBar(),BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildTopBar() {
        JPanel bar=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(0x0B1520),getWidth(),0,new Color(0x081018)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setPaint(new GradientPaint(0,getHeight()-3,new Color(201,168,76,90),0,getHeight(),new Color(201,168,76,0)));
                g2.fillRect(0,getHeight()-3,getWidth(),3);
                g2.setColor(GOLD); g2.setStroke(new BasicStroke(1f));
                g2.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
                g2.dispose();
            }
        };
        bar.setOpaque(false); bar.setPreferredSize(new Dimension(0,60));
        bar.setBorder(new EmptyBorder(0,22,0,22));
        JPanel left=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); left.setOpaque(false);
        left.add(lbl("+",new Font("Georgia",Font.BOLD,20),GOLD));
        left.add(lbl("Pharmacy Management System",F_HEAD,TEXT));
        JPanel right=new JPanel(new FlowLayout(FlowLayout.RIGHT,14,0)); right.setOpaque(false);
        userLabel=lbl("---",F_SMALL,TEXT_MID);
        JButton logoutBtn=pillBtn("Logout",RED);
        logoutBtn.addActionListener(e->{currentUser=null;rootLayout.show(rootPanel,"LOGIN");});
        right.add(userLabel); right.add(logoutBtn);
        bar.add(left,BorderLayout.WEST); bar.add(right,BorderLayout.EAST);
        return bar;
    }

    private JPanel buildSidebar() {
        JPanel sb=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,new Color(0x0A1220),0,getHeight(),new Color(0x060C16)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(new Color(201,168,76,40));
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawLine(getWidth()-1,40,getWidth()-1,getHeight()-40);
                g2.dispose();
            }
        };
        sb.setOpaque(false); sb.setPreferredSize(new Dimension(240,0));
        sb.setLayout(new BoxLayout(sb,BoxLayout.Y_AXIS));
        sb.setBorder(new EmptyBorder(20,0,20,0));

        // Logo block
        JPanel logo=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(201,168,76,18));
                g2.fillRoundRect(12,4,getWidth()-24,getHeight()-8,8,8);
                g2.setColor(new Color(201,168,76,35));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(12,4,getWidth()-24,getHeight()-8,8,8);
                g2.dispose();
            }
        };
        logo.setOpaque(false); logo.setLayout(new FlowLayout(FlowLayout.LEFT,12,10));
        logo.setMaximumSize(new Dimension(240,52)); logo.setAlignmentX(LEFT_ALIGNMENT);
        JPanel lt=new JPanel(); lt.setOpaque(false); lt.setLayout(new BoxLayout(lt,BoxLayout.Y_AXIS));
        lt.add(lbl("PHARMA",new Font("Georgia",Font.BOLD,13),GOLD));
        lt.add(lbl("Management System",new Font("Segoe UI",Font.PLAIN,9),TEXT_DIM));
        logo.add(lbl("+",new Font("Georgia",Font.BOLD,22),GOLD)); logo.add(lt);
        sb.add(logo); sb.add(Box.createVerticalStrut(16));
        sb.add(gradDiv()); sb.add(Box.createVerticalStrut(12));

        JLabel navLbl=lbl("  MENU",F_BADGE,new Color(0x3A5070));
        navLbl.setBorder(new EmptyBorder(0,16,8,0));
        navLbl.setMaximumSize(new Dimension(240,22)); navLbl.setAlignmentX(LEFT_ALIGNMENT);
        sb.add(navLbl);

        Object[][] nav={
            {"DB","Dashboard",  "DASHBOARD",GOLD},
            {"+", "Medicines",  "INVENTORY",TEAL},
            {">>","Search",     "SEARCH",   BLUE},
            {"$", "New Sale",   "SELL",     GREEN},
            {"SL","Sales Log",  "SALESLOG", PURPLE},
            {"RP","Reports",    "REPORTS",  CYAN},
            {"!!","Alerts",     "ALERTS",   AMBER},
            {"AL","Activity Log","ACTLOG",  TEXT_MID},
        };
        for(Object[] n:nav) sb.add(sidebarBtn((String)n[0],(String)n[1],(String)n[2],(Color)n[3]));

        sb.add(Box.createVerticalGlue());
        sb.add(gradDiv()); sb.add(Box.createVerticalStrut(12));

        JPanel bot=new JPanel(); bot.setOpaque(false);
        bot.setLayout(new BoxLayout(bot,BoxLayout.Y_AXIS));
        bot.setBorder(new EmptyBorder(0,18,0,18)); bot.setMaximumSize(new Dimension(240,42));
        JLabel b1=lbl("OOP Lab Project",new Font("Segoe UI",Font.BOLD,11),TEXT_DIM);
        JLabel b2=lbl("Limitless Group  |  IoBM",F_SMALL,new Color(0x2A3A52));
        b1.setAlignmentX(LEFT_ALIGNMENT); b2.setAlignmentX(LEFT_ALIGNMENT);
        bot.add(b1); bot.add(Box.createVerticalStrut(3)); bot.add(b2);
        sb.add(bot); return sb;
    }

    private JPanel gradDiv(){
        JPanel d=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                int w=getWidth();
                g2.setPaint(new GradientPaint(14,0,new Color(201,168,76,0),w/2,0,new Color(201,168,76,45)));
                g2.fillRect(14,0,w/2-14,1);
                g2.setPaint(new GradientPaint(w/2,0,new Color(201,168,76,45),w-14,0,new Color(201,168,76,0)));
                g2.fillRect(w/2,0,w/2-14,1);
                g2.dispose();
            }
        };
        d.setOpaque(false); d.setMaximumSize(new Dimension(240,1)); d.setAlignmentX(LEFT_ALIGNMENT);
        return d;
    }

    private JButton sidebarBtn(String icon,String title,String card,Color accent){
        final int myIdx=sidebarIdx;
        JButton btn=new JButton(){
            boolean hov=false;
            {
                setFont(F_BODY); setHorizontalAlignment(SwingConstants.LEFT);
                setBorderPainted(false); setFocusPainted(false); setContentAreaFilled(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setMaximumSize(new Dimension(240,48)); setPreferredSize(new Dimension(240,48));
                setBorder(new EmptyBorder(0,0,0,0));
                addMouseListener(new MouseAdapter(){
                    public void mouseEntered(MouseEvent e){hov=true;repaint();}
                    public void mouseExited(MouseEvent e){hov=false;repaint();}
                });
                addActionListener(e->{
                    activeSidebarIndex=myIdx; showPage(card);
                    for(JButton b:sidebarBtns) if(b!=null) b.repaint();
                });
            }
            protected void paintComponent(Graphics g){
                boolean active=activeSidebarIndex==myIdx;
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                if(active){
                    g2.setPaint(new GradientPaint(0,0,
                        new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),32),
                        getWidth(),0,new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),0)));
                    g2.fillRect(0,0,getWidth(),getHeight());
                    g2.setColor(accent);
                    g2.fillRoundRect(0,8,4,getHeight()-16,4,4);
                } else if(hov){
                    g2.setColor(new Color(0x111D2C));
                    g2.fillRect(0,0,getWidth(),getHeight());
                    g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),60));
                    g2.fillRoundRect(0,14,2,getHeight()-28,2,2);
                }
                // Icon badge
                int ix=16,iy=getHeight()/2-11,iw=28,ih=22;
                if(active){
                    g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),25));
                    g2.fillRoundRect(ix,iy,iw,ih,5,5);
                    g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),70));
                    g2.setStroke(new BasicStroke(0.8f));
                    g2.drawRoundRect(ix,iy,iw,ih,5,5);
                }
                // Icon text
                g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                g2.setColor(active?accent:(hov?TEXT_MID:TEXT_DIM));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(icon,ix+(iw-fm.stringWidth(icon))/2,iy+(ih+fm.getAscent()-fm.getDescent())/2);
                // Title text
                g2.setFont(active?new Font("Segoe UI",Font.BOLD,13):F_BODY);
                g2.setColor(active?accent:(hov?TEXT:TEXT_DIM));
                g2.drawString(title,ix+iw+12,getHeight()/2+5);
                g2.dispose();
            }
        };
        if(sidebarIdx<sidebarBtns.length) sidebarBtns[sidebarIdx++]=btn;
        return btn;
    }

    private JPanel buildContent(){
        contentLayout=new CardLayout(); contentArea=new JPanel(contentLayout);
        contentArea.setBackground(BG);
        contentArea.add(buildDashboard(),"DASHBOARD"); contentArea.add(buildInventory(),"INVENTORY");
        contentArea.add(buildSearch(),"SEARCH");       contentArea.add(buildSell(),"SELL");
        contentArea.add(buildSalesLog(),"SALESLOG");   contentArea.add(buildReports(),"REPORTS");
        contentArea.add(buildAlerts(),"ALERTS");       contentArea.add(buildActLog(),"ACTLOG");
        contentLayout.show(contentArea,"DASHBOARD"); return contentArea;
    }

    private void showPage(String card){
        contentLayout.show(contentArea,card);
        if(card.equals("INVENTORY"))refreshInventory();
        if(card.equals("ALERTS"))refreshAlerts();
        if(card.equals("SALESLOG"))refreshSalesLog();
        if(card.equals("ACTLOG"))refreshActLog();
        if(card.equals("REPORTS")&&reportsArea!=null) reportsArea.setText("Choose a report above.");
    }

    private JPanel buildStatusBar(){
        JPanel bar=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                g.setColor(new Color(0x060C14));g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(BORDER);((Graphics2D)g).drawLine(0,0,getWidth(),0);
            }
        };
        bar.setOpaque(false); bar.setPreferredSize(new Dimension(0,26));
        bar.setBorder(new EmptyBorder(0,18,0,18));
        statusUser=lbl("",F_SMALL,TEXT_DIM); statusMeds=lbl("",F_SMALL,TEXT_DIM); statusTime=lbl("",F_SMALL,TEXT_DIM);
        JPanel left=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)); left.setOpaque(false);
        left.add(statusUser); left.add(lbl("   |   ",F_SMALL,BORDER)); left.add(statusMeds);
        bar.add(left,BorderLayout.WEST); bar.add(statusTime,BorderLayout.EAST);
        return bar;
    }

    private JPanel buildDashboard(){
        JPanel p=page(); p.setLayout(new BorderLayout(0,22)); p.setBorder(new EmptyBorder(28,32,28,32));
        JPanel hdr=new JPanel(new BorderLayout()); hdr.setOpaque(false);
        hdr.add(vstack(lbl("Dashboard",F_TITLE,TEXT),Box.createVerticalStrut(3),
            lbl("System overview  |  Live statistics",F_BODY,TEXT_DIM)),BorderLayout.WEST);
        p.add(hdr,BorderLayout.NORTH);
        statMeds=lbl("--",F_STAT,GOLD); statAlerts=lbl("--",F_STAT,RED);
        statRevenue=lbl("--",F_STAT,GREEN); statLow=lbl("--",F_STAT,AMBER);
        JPanel stats=new JPanel(new GridLayout(1,4,16,0)); stats.setOpaque(false);
        stats.add(statCard("Total Medicines",statMeds,GOLD,"MED"));
        stats.add(statCard("Active Alerts",statAlerts,RED,"ALT"));
        stats.add(statCard("Total Revenue Rs",statRevenue,GREEN,"REV"));
        stats.add(statCard("Low Stock Items",statLow,AMBER,"LOW"));
        JPanel qa=new JPanel(new GridLayout(2,3,12,12)); qa.setOpaque(false);
        qa.add(qaBtn("[+]  View Medicines","INVENTORY",TEAL,BLUE));
        qa.add(qaBtn("[$]  New Sale","SELL",GREEN,TEAL));
        qa.add(qaBtn("[?]  Search","SEARCH",BLUE,PURPLE));
        qa.add(qaBtn("[R]  Sales Reports","REPORTS",PURPLE,BLUE));
        qa.add(qaBtn("[!]  Alert Report","ALERTS",AMBER,RED));
        qa.add(qaBtn("[A]  Activity Log","ACTLOG",CYAN,TEAL));
        JPanel center=new JPanel(new BorderLayout(0,22)); center.setOpaque(false);
        center.add(stats,BorderLayout.NORTH);
        JPanel qaSec=new JPanel(new BorderLayout(0,12)); qaSec.setOpaque(false);
        qaSec.add(lbl("Quick Actions",F_HEAD,TEXT),BorderLayout.NORTH);
        qaSec.add(qa,BorderLayout.CENTER); center.add(qaSec,BorderLayout.CENTER);
        p.add(center,BorderLayout.CENTER); return p;
    }

    private JPanel statCard(String label,JLabel val,Color accent,String tag){
        JPanel c=new JPanel(){
            boolean hov=false;
            {addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){hov=true;repaint();}
                public void mouseExited(MouseEvent e){hov=false;repaint();}
            });}
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,CARD,0,getHeight(),CARD2));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(hov?accent:BORDER); g2.setStroke(new BasicStroke(hov?1.5f:1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                if(hov){g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),20));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);}
                g2.setColor(accent); g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16,getHeight()-2,getWidth()-16,getHeight()-2);
                g2.dispose();
            }
        };
        c.setOpaque(false); c.setLayout(new BorderLayout()); c.setBorder(new EmptyBorder(18,20,20,20));
        c.add(lbl(tag,F_BADGE,new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),80)),BorderLayout.NORTH);
        c.add(val,BorderLayout.CENTER);
        c.add(lbl(label,F_SMALL,TEXT_DIM),BorderLayout.SOUTH);
        return c;
    }

    private JButton qaBtn(String text,String card,Color c1,Color c2){
        JButton btn=new JButton(text){
            boolean hov=false;
            {addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){hov=true;repaint();}
                public void mouseExited(MouseEvent e){hov=false;repaint();}
            }); addActionListener(e->showPage(card));}
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                if(hov){g2.setPaint(new GradientPaint(0,0,c1,getWidth(),getHeight(),c2));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);setForeground(new Color(0x080E1A));}
                else{g2.setColor(CARD);g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                    g2.setColor(c1);g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);setForeground(c1);}
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(F_BTN); btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); btn.setBorder(new EmptyBorder(18,20,18,20));
        return btn;
    }

    private void refreshDashboard(){
        if(statMeds==null)return;
        statMeds.setText(String.valueOf(Medicine.getCount()));
        statAlerts.setText(String.valueOf(alertSystem.getAlertCount()));
        statRevenue.setText(String.format("%.0f",sales.getTotalRevenue()));
        int low=0; Medicine[] inv=Medicine.getInventory();
        for(int i=0;i<Medicine.getCount();i++) if(inv[i].getQuantity()<=inv[i].getMinStockThreshold())low++;
        statLow.setText(String.valueOf(low));
        if(statusUser!=null&&currentUser!=null)
            statusUser.setText("  "+fmtName(currentUser.getUsername())+"  ["+currentUser.getRole()+"]");
        if(statusMeds!=null) statusMeds.setText("Medicines: "+Medicine.getCount());
    }

    private JPanel buildInventory(){
        JPanel p=page(); p.setLayout(new BorderLayout(0,14)); p.setBorder(new EmptyBorder(28,32,28,32));
        JPanel hdr=new JPanel(new BorderLayout()); hdr.setOpaque(false);
        hdr.add(vstack(lbl("Medicines",F_TITLE,TEXT),Box.createVerticalStrut(2),
            lbl("Full inventory  |  Type to filter  |  Amber=low stock  |  Red=expired",F_SMALL,TEXT_DIM)),BorderLayout.WEST);
        liveSearch=loginField("Filter by name, ID, formula...");
        liveSearch.setPreferredSize(new Dimension(240,38));
        liveSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){filterTable();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){filterTable();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){filterTable();}
        });
        JPanel btnRow=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); btnRow.setOpaque(false);
        btnRow.add(liveSearch); btnRow.add(Box.createHorizontalStrut(4));
        JButton addBtn=accentBtn("+ Add",GREEN),editBtn=accentBtn("Edit",BLUE),
            delBtn=accentBtn("Delete",RED),refBtn=accentBtn("Refresh",TEXT_DIM);
        btnRow.add(refBtn);btnRow.add(editBtn);btnRow.add(addBtn);btnRow.add(delBtn);
        hdr.add(btnRow,BorderLayout.EAST); p.add(hdr,BorderLayout.NORTH);
        String[] cols={"ID","Name","Company","Price","Expiry","Qty","Min","Formula","Rx","Status"};
        inventoryModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        inventoryTable=epicTable(inventoryModel);
        p.add(scroll(inventoryTable),BorderLayout.CENTER);
        refBtn.addActionListener(e->refreshInventory());
        addBtn.addActionListener(e->showAddDialog());
        editBtn.addActionListener(e->{int row=inventoryTable.getSelectedRow();
            if(row<0){showToast("Select a medicine.",AMBER);return;}
            showEditDialog((int)inventoryModel.getValueAt(row,0));});
        delBtn.addActionListener(e->{
            if(currentUser==null||!currentUser.getRole().equals("Admin")){showToast("Admin only.",RED);return;}
            int row=inventoryTable.getSelectedRow();
            if(row<0){showToast("Select a medicine.",AMBER);return;}
            int id=(int)inventoryModel.getValueAt(row,0); Medicine m=Medicine.getMedicineById(id);
            if(showConfirm("Delete '"+m.getName()+"'?")){
                Medicine.deleteMedicine(id); FileManager.saveInventory();
                FileManager.logActivity(fmtName(currentUser.getUsername())+" deleted: "+m.getName());
                refreshInventory(); refreshDashboard(); showToast("Deleted: "+m.getName(),GREEN);}});
        refreshInventory(); return p;
    }

    private void filterTable(){
        if(inventoryModel==null)return;
        inventoryModel.setRowCount(0);
        String kw=liveSearch==null?"":liveSearch.getText().trim().toLowerCase();
        Medicine[] inv=Medicine.getInventory();
        for(int i=0;i<Medicine.getCount();i++){
            Medicine m=inv[i];
            if(kw.isEmpty()||m.getName().toLowerCase().contains(kw)
               ||m.getCompany().toLowerCase().contains(kw)
               ||m.getFormulaCategory().toLowerCase().contains(kw)
               ||String.valueOf(m.getId()).contains(kw)){
                boolean expired=DateHelper.isExpired(m.getExpiryDate());
                boolean low=m.getQuantity()<=m.getMinStockThreshold();
                inventoryModel.addRow(new Object[]{m.getId(),m.getName(),m.getCompany(),
                    String.format("%.2f",m.getPrice()),m.getExpiryDate(),m.getQuantity(),
                    m.getMinStockThreshold(),m.getFormulaCategory(),
                    m.isPrescriptionRequired()?"Yes":"No",expired?"EXPIRED":low?"LOW STOCK":"OK"});
            }
        }
        inventoryTable.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int row,int col){
                super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                setFont(col==1?F_SUB:F_BODY); setBorder(new EmptyBorder(5,10,5,10));
                String st=inventoryModel.getRowCount()>row?inventoryModel.getValueAt(row,9).toString():"OK";
                if(sel){setBackground(new Color(0x162840));setForeground(TEXT);}
                else if(st.equals("EXPIRED")){setBackground(new Color(0x1E0A0A));setForeground(col==9?RED:TEXT_MID);}
                else if(st.equals("LOW STOCK")){setBackground(new Color(0x1E1400));setForeground(col==9?AMBER:TEXT_MID);}
                else{setBackground(row%2==0?CARD:PANEL);setForeground(col==9?GREEN:TEXT);}
                if(col==9){setFont(F_BADGE);setHorizontalAlignment(CENTER);}
                return this;
            }
        });
    }

    private void refreshInventory(){if(liveSearch!=null)liveSearch.setText("");filterTable();}

    private JPanel buildSearch(){
        JPanel p=page(); p.setLayout(new BorderLayout(0,16)); p.setBorder(new EmptyBorder(28,32,28,32));
        p.add(vstack(lbl("Search Medicines",F_TITLE,TEXT),Box.createVerticalStrut(2),
            lbl("Search by name, ID, or find alternatives by formula category",F_SMALL,TEXT_DIM)),BorderLayout.NORTH);
        JTextField sf=loginField("Medicine name, ID, or formula...");
        sf.setPreferredSize(new Dimension(360,40));
        JButton byName=accentBtn("Search Name",BLUE),byId=accentBtn("Search ID",TEAL),altBtn=accentBtn("Alternatives",PURPLE);
        JPanel row=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); row.setOpaque(false);
        row.add(sf);row.add(byName);row.add(byId);row.add(altBtn);
        DefaultTableModel sm=new DefaultTableModel(new String[]{"ID","Name","Company","Price","Expiry","Qty","Formula","Rx"},0){
            public boolean isCellEditable(int r,int c){return false;}};
        JTable st=epicTable(sm);
        JTextArea altArea=monoArea(); altArea.setForeground(GREEN);
        JPanel center=new JPanel(new BorderLayout(0,12)); center.setOpaque(false);
        center.add(row,BorderLayout.NORTH); center.add(scroll(st),BorderLayout.CENTER);
        JScrollPane altSp=scroll(altArea); altSp.setPreferredSize(new Dimension(0,120));
        center.add(altSp,BorderLayout.SOUTH); p.add(center,BorderLayout.CENTER);
        byName.addActionListener(e->{sm.setRowCount(0);altArea.setText("");
            String kw=sf.getText().trim();if(kw.isEmpty())return;
            Medicine[] res=Medicine.searchByName(kw);
            if(res.length==0){altArea.setText("No results for: "+kw);return;}
            for(Medicine m:res){sm.addRow(new Object[]{m.getId(),m.getName(),m.getCompany(),
                String.format("%.2f",m.getPrice()),m.getExpiryDate(),m.getQuantity(),
                m.getFormulaCategory(),m.isPrescriptionRequired()?"Yes":"No"});
            if(m.getQuantity()==0){Medicine[] alts=Medicine.findAlternatives(m.getFormulaCategory(),m.getId());
                if(alts.length>0){StringBuilder sb=new StringBuilder(m.getName()+" OUT OF STOCK.\nAlternatives ("+m.getFormulaCategory()+"):\n");
                    for(Medicine a:alts)sb.append("  > ").append(a.getName()).append("  Rs.").append(String.format("%.2f",a.getPrice())).append("  Qty:").append(a.getQuantity()).append("\n");
                    altArea.setText(sb.toString());}}}
            showToast(res.length+" result(s) found.",GREEN);});
        byId.addActionListener(e->{sm.setRowCount(0);altArea.setText("");
            try{Medicine m=Medicine.searchById(Integer.parseInt(sf.getText().trim()));
                if(m==null){altArea.setText("Not found.");return;}
                sm.addRow(new Object[]{m.getId(),m.getName(),m.getCompany(),
                    String.format("%.2f",m.getPrice()),m.getExpiryDate(),m.getQuantity(),
                    m.getFormulaCategory(),m.isPrescriptionRequired()?"Yes":"No"});}
            catch(NumberFormatException ex){altArea.setText("Enter a valid numeric ID.");}});
        altBtn.addActionListener(e->{sm.setRowCount(0);altArea.setText("");
            String f=sf.getText().trim();if(f.isEmpty()){altArea.setText("Enter formula name.");return;}
            Medicine[] alts=Medicine.findAlternatives(f,-1);
            if(alts.length==0){altArea.setText("No alternatives for: "+f);return;}
            for(Medicine m:alts)sm.addRow(new Object[]{m.getId(),m.getName(),m.getCompany(),
                String.format("%.2f",m.getPrice()),m.getExpiryDate(),m.getQuantity(),
                m.getFormulaCategory(),m.isPrescriptionRequired()?"Yes":"No"});
            showToast(alts.length+" alternative(s) found.",PURPLE);});
        return p;
    }

    private JPanel buildSell(){
        JPanel p=page(); p.setLayout(new BorderLayout(20,0)); p.setBorder(new EmptyBorder(28,32,28,32));
        JPanel form=new JPanel(); form.setOpaque(false);
        form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS)); form.setPreferredSize(new Dimension(380,0));
        JTextField fId=formField("Medicine ID"),fQty=formField("Quantity"),
            fDisc=formField("Discount % (Admin only)"),fRef=formField("Bill ID to refund");
        JButton sellBtn=goldBtn("Process Sale"),refundBtn=accentBtn("Process Refund",AMBER);
        sellBtn.setAlignmentX(LEFT_ALIGNMENT); sellBtn.setMaximumSize(new Dimension(360,46));
        refundBtn.setAlignmentX(LEFT_ALIGNMENT); refundBtn.setMaximumSize(new Dimension(360,42));
        form.add(lbl("New Sale",F_TITLE,TEXT)); form.add(Box.createVerticalStrut(3));
        form.add(lbl("Sell medicines and generate bills",F_SMALL,TEXT_DIM)); form.add(Box.createVerticalStrut(22));
        form.add(fl("Medicine ID")); form.add(Box.createVerticalStrut(4)); form.add(fId);
        form.add(Box.createVerticalStrut(12)); form.add(fl("Quantity")); form.add(Box.createVerticalStrut(4)); form.add(fQty);
        form.add(Box.createVerticalStrut(12)); form.add(fl("Discount %")); form.add(Box.createVerticalStrut(4)); form.add(fDisc);
        form.add(Box.createVerticalStrut(18)); form.add(sellBtn); form.add(Box.createVerticalStrut(24));
        JSeparator sep=new JSeparator(); sep.setForeground(BORDER); sep.setBackground(BORDER);
        sep.setMaximumSize(new Dimension(360,1)); sep.setAlignmentX(LEFT_ALIGNMENT); form.add(sep);
        form.add(Box.createVerticalStrut(18)); form.add(lbl("Refund",F_SUB,TEXT_MID));
        form.add(Box.createVerticalStrut(8)); form.add(fl("Bill ID")); form.add(Box.createVerticalStrut(4)); form.add(fRef);
        form.add(Box.createVerticalStrut(12)); form.add(refundBtn); form.add(Box.createVerticalGlue());
        JPanel right=new JPanel(new BorderLayout(0,10)); right.setOpaque(false);
        right.add(lbl("Receipt",F_HEAD,TEXT),BorderLayout.NORTH);
        billArea=monoArea(); billArea.setForeground(GREEN); billArea.setText("Bill will appear here after a sale...");
        right.add(scroll(billArea),BorderLayout.CENTER);
        p.add(form,BorderLayout.WEST); p.add(right,BorderLayout.CENTER);
        sellBtn.addActionListener(e->{
            try{int id=Integer.parseInt(fId.getText().trim()),qty=Integer.parseInt(fQty.getText().trim());
                double disc=0;
                if(!fDisc.getText().trim().isEmpty())disc=Double.parseDouble(fDisc.getText().trim());
                if(disc>0&&(currentUser==null||!currentUser.getRole().equals("Admin"))){showToast("Only Admin can apply discounts.",RED);return;}
                String nm=currentUser!=null?fmtName(currentUser.getUsername()):"Staff";
                // Check expiry before attempting sale
                Medicine chk=Medicine.searchById(id);
                if(chk!=null && DateHelper.isExpired(chk.getExpiryDate())){
                    showToast("BLOCKED: "+chk.getName()+" is expired!",RED);
                    billArea.setText(
                        "====== SALE BLOCKED ======\n\n" +
                        "Medicine : " + chk.getName() + "\n" +
                        "Expired  : " + chk.getExpiryDate() + "\n\n" +
                        "Expired medicines cannot be sold.\n" +
                        "Please remove this medicine from\n" +
                        "inventory or contact your Admin.\n\n" +
                        "==========================");
                    return;
                }
                Bill bill=sales.sellMedicine(id,qty,nm);
                if(bill!=null){
                    if(disc>0) sales.applyDiscount(bill.getBillID(),disc);
                    StringBuilder sb=new StringBuilder();
                    sb.append("====== PHARMACY BILL ======\n");
                    sb.append(String.format("%-18s %d%n","Bill ID:",bill.getBillID()));
                    sb.append(String.format("%-18s %s%n","Medicine:",bill.getMedicineName()));
                    sb.append(String.format("%-18s %d%n","Quantity:",bill.getQuantitySold()));
                    sb.append(String.format("%-18s Rs. %.2f%n","Unit Price:",bill.getPricePerUnit()));
                    if(bill.getDiscountPercent()>0)sb.append(String.format("%-18s %.1f%%%n","Discount:",bill.getDiscountPercent()));
                    sb.append("---------------------------\n");
                    sb.append(String.format("%-18s Rs. %.2f%n","TOTAL:",bill.getTotalAmount()));
                    sb.append("---------------------------\n");
                    sb.append(String.format("%-18s %s%n","Date:",bill.getDateOfSale()));
                    sb.append(String.format("%-18s %s%n","Sold By:",bill.getSoldBy()));
                    sb.append("===========================\n");
                    billArea.setText(sb.toString()); FileManager.saveInventory(); refreshDashboard();
                    fId.setText("");fQty.setText("");fDisc.setText("");
                    showToast("Sale complete. Bill #"+bill.getBillID(),GREEN);
                } else {
                    showToast("Sale failed. Check ID, quantity, or stock.",RED);
                }}
            catch(NumberFormatException ex){showToast("Enter valid ID and Quantity.",AMBER);}});
        refundBtn.addActionListener(e->{
            if(currentUser==null||!currentUser.getRole().equals("Admin")){showToast("Only Admin can refund.",RED);return;}
            try{boolean ok=sales.processRefund(Integer.parseInt(fRef.getText().trim()));
                if(ok){FileManager.saveInventory();refreshDashboard();
                    showToast("Refund processed.",GREEN);billArea.setText("Refund complete.");}
                else showToast("Refund failed. Check Bill ID.",RED);}
            catch(NumberFormatException ex){showToast("Enter a valid Bill ID.",AMBER);}});
        return p;
    }

    private JPanel buildSalesLog(){
        JPanel p=page(); p.setLayout(new BorderLayout(0,14)); p.setBorder(new EmptyBorder(28,32,28,32));
        JPanel hdr=hdrRow("Sales Log","All transactions recorded with timestamps"); hdr.setOpaque(false);
        JButton ref=accentBtn("Refresh",TEXT_DIM); ref.addActionListener(e->refreshSalesLog());
        hdr.add(ref,BorderLayout.EAST); p.add(hdr,BorderLayout.NORTH);
        salesLogArea=monoArea(); p.add(scroll(salesLogArea),BorderLayout.CENTER);
        refreshSalesLog(); return p;
    }

    private void refreshSalesLog(){
        if(salesLogArea==null)return;
        ArrayList<Bill> log=sales.getSalesLog();
        if(log.isEmpty()){salesLogArea.setText("No sales recorded yet.");return;}
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%-8s %-22s %-6s %-12s %-22s %-14s%n","Bill","Medicine","Qty","Total Rs.","Date","Sold By"));
        sb.append("-".repeat(88)).append("\n");
        for(Bill b:log)sb.append(String.format("%-8d %-22s %-6d %-12.2f %-22s %-14s%s%n",
            b.getBillID(),b.getMedicineName(),b.getQuantitySold(),b.getTotalAmount(),
            b.getDateOfSale(),b.getSoldBy(),b.isRefunded()?" [REFUNDED]":""));
        salesLogArea.setText(sb.toString());
    }

    private JPanel buildReports(){
        JPanel p=page(); p.setLayout(new BorderLayout(0,14)); p.setBorder(new EmptyBorder(28,32,28,32));
        JPanel hdr=hdrRow("Sales Reports","Daily  |  Weekly  |  Monthly  |  Best Seller  |  Profit & Loss");
        hdr.setOpaque(false);
        JPanel btnRow=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); btnRow.setOpaque(false);
        JButton daily=accentBtn("Today",BLUE),weekly=accentBtn("Weekly",GREEN),
            monthly=accentBtn("Monthly",PURPLE),best=accentBtn("Best Seller",GOLD),pl=accentBtn("P & L",AMBER);
        btnRow.add(daily);btnRow.add(weekly);btnRow.add(monthly);btnRow.add(best);btnRow.add(pl);
        hdr.add(btnRow,BorderLayout.EAST); p.add(hdr,BorderLayout.NORTH);
        reportsArea=monoArea(); reportsArea.setText("Choose a report type above.");
        p.add(scroll(reportsArea),BorderLayout.CENTER);
        String today=LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        daily.addActionListener(e->capture(()->sales.getDailySummary(today)));
        weekly.addActionListener(e->capture(()->sales.getWeeklySummary()));
        monthly.addActionListener(e->capture(()->sales.getMonthlySummary()));
        best.addActionListener(e->capture(()->sales.getBestSellingMedicine()));
        pl.addActionListener(e->capture(()->sales.calculateProfitLoss()));
        return p;
    }

    private void capture(Runnable r){
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        PrintStream old=System.out; System.setOut(new PrintStream(b));
        r.run(); System.setOut(old);
        reportsArea.setForeground(GREEN); reportsArea.setText(b.toString());
    }

    private JPanel buildAlerts(){
        JPanel p=page(); p.setLayout(new BorderLayout(0,14)); p.setBorder(new EmptyBorder(28,32,28,32));
        JPanel hdr=hdrRow("Alert Report","Expiry warnings  |  Low stock alerts  |  Auto-runs on startup");
        hdr.setOpaque(false);
        JButton rerun=accentBtn("Re-run Checks",AMBER); rerun.addActionListener(e->refreshAlerts());
        hdr.add(rerun,BorderLayout.EAST); p.add(hdr,BorderLayout.NORTH);
        alertsArea=monoArea(); alertsArea.setForeground(AMBER);
        p.add(scroll(alertsArea),BorderLayout.CENTER); refreshAlerts(); return p;
    }

    private void refreshAlerts(){
        if(alertsArea==null)return;
        alertSystem.checkAllOnStartup(); String[] hist=alertSystem.getAlertHistory();
        if(hist.length==0){alertsArea.setForeground(GREEN);
            alertsArea.setText("OK  All medicines are within safe parameters.\nNo issues detected.");return;}
        alertsArea.setForeground(AMBER);
        StringBuilder sb=new StringBuilder();
        sb.append("!! ALERT REPORT  -  ").append(DateHelper.todayAsString()).append("\n");
        sb.append("=".repeat(65)).append("\n\n");
        for(String al:hist)sb.append("  > ").append(al).append("\n");
        sb.append("\n").append("=".repeat(65)).append("\nTotal: ").append(hist.length).append(" alert(s)");
        alertsArea.setText(sb.toString()); refreshDashboard();
    }

    private JPanel buildActLog(){
        JPanel p=page(); p.setLayout(new BorderLayout(0,14)); p.setBorder(new EmptyBorder(28,32,28,32));
        JPanel hdr=hdrRow("Activity Log","Admin actions recorded with timestamps"); hdr.setOpaque(false);
        JButton ref=accentBtn("Refresh",TEXT_DIM); ref.addActionListener(e->refreshActLog());
        hdr.add(ref,BorderLayout.EAST); p.add(hdr,BorderLayout.NORTH);
        actLogArea=monoArea(); actLogArea.setForeground(TEXT_MID);
        p.add(scroll(actLogArea),BorderLayout.CENTER); refreshActLog(); return p;
    }

    private void refreshActLog(){
        if(actLogArea==null)return;
        File f=new File("data/activity_log.txt");
        if(!f.exists()){actLogArea.setText("No activity recorded yet.");return;}
        try{BufferedReader r=new BufferedReader(new FileReader(f));
            StringBuilder sb=new StringBuilder(); String line;
            while((line=r.readLine())!=null)sb.append(line).append("\n");
            r.close(); actLogArea.setText(sb.length()==0?"No activity recorded yet.":sb.toString());}
        catch(Exception ex){actLogArea.setText("Error reading log.");}
    }

    private void showAlertDialog(String[] alerts){
        JDialog d=new JDialog(this,true);
        d.setUndecorated(true); d.setSize(640,430); d.setLocationRelativeTo(this);
        JPanel root=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,new Color(0x0E1A2A),0,getHeight(),new Color(0x090F1A)));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(AMBER);g2.setStroke(new BasicStroke(2f));g2.drawLine(50,0,getWidth()-50,0);
                g2.setColor(BORDER);g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        JPanel header=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                g.setColor(new Color(0x08121E));g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(BORDER);((Graphics2D)g).drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };
        header.setOpaque(false); header.setBorder(new EmptyBorder(16,22,16,22));
        JLabel icon=lbl("!!",new Font("Georgia",Font.BOLD,24),AMBER);
        JPanel tb=new JPanel(); tb.setOpaque(false); tb.setLayout(new BoxLayout(tb,BoxLayout.Y_AXIS));
        tb.add(lbl("Startup Alert Report",new Font("Georgia",Font.BOLD,16),AMBER));
        tb.add(Box.createVerticalStrut(3));
        tb.add(lbl(alerts.length+" issue(s) detected  -  "+DateHelper.todayAsString(),F_SMALL,TEXT_DIM));
        JPanel ir=new JPanel(new FlowLayout(FlowLayout.LEFT,14,0)); ir.setOpaque(false);
        ir.add(icon);ir.add(tb); header.add(ir,BorderLayout.WEST);
        JPanel list=new JPanel(); list.setBackground(new Color(0x0A1018));
        list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS)); list.setBorder(new EmptyBorder(14,18,14,18));
        for(String al:alerts){
            Color rc=al.contains("EXPIRED")?RED:al.contains("LOW")?AMBER:GREEN;
            JPanel row=new JPanel(new BorderLayout()){
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(rc.getRed(),rc.getGreen(),rc.getBlue(),18));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                    g2.setColor(new Color(rc.getRed(),rc.getGreen(),rc.getBlue(),80));
                    g2.fillRect(0,0,3,getHeight()); g2.dispose();
                }
            };
            row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,36));
            row.setBorder(new EmptyBorder(7,12,7,12));
            row.add(lbl(al,new Font("Consolas",Font.PLAIN,11),rc),BorderLayout.WEST);
            list.add(row); list.add(Box.createVerticalStrut(4));
        }
        JScrollPane sp=scroll(list); sp.setBorder(BorderFactory.createMatteBorder(1,0,1,0,BORDER));
        JPanel footer=new JPanel(new FlowLayout(FlowLayout.CENTER)); footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(12,0,14,0));
        JButton ok=goldBtn("  Acknowledge  "); ok.addActionListener(e->d.dispose()); footer.add(ok);
        root.add(header,BorderLayout.NORTH); root.add(sp,BorderLayout.CENTER); root.add(footer,BorderLayout.SOUTH);
        d.setContentPane(root); d.setVisible(true);
    }

    private void showToast(String msg,Color color){
        if(toastTimer!=null&&toastTimer.isRunning())toastTimer.stop();
        JPanel glass=(JPanel)getGlassPane(); glass.setLayout(null); glass.setVisible(true);
        JLabel toast=new JLabel("  "+msg+"  "){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),230));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(new Color(0,0,0,50));g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        toast.setFont(F_BTN); toast.setForeground(new Color(0x060E18)); toast.setOpaque(false);
        toast.setBorder(new EmptyBorder(10,16,10,16));
        Dimension ps=toast.getPreferredSize(); int tw=Math.max(ps.width+40,260),th=44;
        toast.setBounds(getWidth()-tw-24,getHeight()-th-50,tw,th);
        glass.removeAll();glass.add(toast);glass.revalidate();glass.repaint();
        toastTimer=new Timer(2800,e->{glass.removeAll();glass.setVisible(false);glass.repaint();});
        toastTimer.setRepeats(false); toastTimer.start();
    }

    private boolean showConfirm(String msg){
        return JOptionPane.showConfirmDialog(this,msg,"Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
    }

    private void showAddDialog(){
        JDialog d=mkDialog("Add New Medicine",500,560);
        JPanel form=new JPanel(new GridLayout(0,2,10,10)); form.setOpaque(false); form.setBorder(new EmptyBorder(20,24,20,24));
        JTextField fN=df(),fC=df(),fP=df(),fE=df(),fQ=df(),fM=df(),fF=df();
        JComboBox<String> fRx=new JComboBox<>(new String[]{"No","Yes"}); styleCombo(fRx);
        form.add(fl("Name:"));form.add(fN); form.add(fl("Company:"));form.add(fC);
        form.add(fl("Price (Rs.):"));form.add(fP); form.add(fl("Expiry (dd/MM/yyyy):"));form.add(fE);
        form.add(fl("Quantity:"));form.add(fQ); form.add(fl("Min Threshold:"));form.add(fM);
        form.add(fl("Formula Category:"));form.add(fF); form.add(fl("Prescription:"));form.add(fRx);
        JButton save=goldBtn("Add Medicine");
        save.addActionListener(e->{
            try{Medicine m=new Medicine(fN.getText().trim(),fC.getText().trim(),
                    Double.parseDouble(fP.getText().trim()),fE.getText().trim(),
                    Integer.parseInt(fQ.getText().trim()),Integer.parseInt(fM.getText().trim()),
                    fF.getText().trim(),fRx.getSelectedItem().equals("Yes"));
                Medicine.addMedicine(m); FileManager.saveInventory();
                FileManager.logActivity(fmtName(currentUser.getUsername())+" added: "+m.getName()+" (ID:"+m.getId()+")");
                refreshInventory(); refreshDashboard(); d.dispose(); showToast("Added: "+m.getName(),GREEN);}
            catch(Exception ex){showToast("Check all fields.",RED);}});
        JPanel bot=new JPanel(new FlowLayout()); bot.setOpaque(false); bot.add(save);
        d.add(form,BorderLayout.CENTER); d.add(bot,BorderLayout.SOUTH); d.setVisible(true);
    }

    private void showEditDialog(int id){
        Medicine m=Medicine.getMedicineById(id); if(m==null)return;
        JDialog d=mkDialog("Update: "+m.getName(),420,250);
        JPanel form=new JPanel(new GridLayout(0,2,10,10)); form.setOpaque(false); form.setBorder(new EmptyBorder(20,24,20,24));
        JTextField fP=df(),fQ=df(); fP.setText(String.valueOf(m.getPrice())); fQ.setText(String.valueOf(m.getQuantity()));
        form.add(fl("New Price (Rs.):")); form.add(fP); form.add(fl("New Quantity:")); form.add(fQ);
        JButton save=goldBtn("Update");
        save.addActionListener(e->{
            try{if(!fP.getText().trim().isEmpty())m.setPrice(Double.parseDouble(fP.getText().trim()));
                if(!fQ.getText().trim().isEmpty())m.setQuantity(Integer.parseInt(fQ.getText().trim()));
                FileManager.saveInventory();
                FileManager.logActivity(fmtName(currentUser.getUsername())+" updated ID:"+id);
                refreshInventory(); d.dispose(); showToast("Updated.",GREEN);}
            catch(Exception ex){showToast("Invalid input.",RED);}});
        JPanel bot=new JPanel(new FlowLayout()); bot.setOpaque(false); bot.add(save);
        d.add(form,BorderLayout.CENTER); d.add(bot,BorderLayout.SOUTH); d.setVisible(true);
    }

    private void startClock(){
        new Timer(1000,e->{
            String now=LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE  dd MMM yyyy  HH:mm:ss"));
            if(clockLabel!=null)clockLabel.setText(now);
            if(statusTime!=null)statusTime.setText(now+"  ");
        }).start();
    }

    private JPanel page(){JPanel p=new JPanel();p.setBackground(BG);return p;}
    private JLabel lbl(String t,Font f,Color c){JLabel l=new JLabel(t);l.setFont(f);l.setForeground(c);return l;}
    private JPanel vstack(Component... cs){
        JPanel p=new JPanel(); p.setOpaque(false); p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        for(Component c:cs)p.add(c); return p;
    }
    private JPanel hdrRow(String title,String sub){
        JPanel h=new JPanel(new BorderLayout()); h.setOpaque(false);
        h.add(vstack(lbl(title,F_TITLE,TEXT),Box.createVerticalStrut(2),lbl(sub,F_SMALL,TEXT_DIM)),BorderLayout.WEST);
        return h;
    }
    private JLabel fl(String t){return lbl(t,F_SUB,TEXT_MID);}

    private JTextField loginField(String hint){
        JTextField f=new JTextField(){
            protected void paintComponent(Graphics g){super.paintComponent(g);
                if(getText().isEmpty()){Graphics2D g2=(Graphics2D)g.create();
                    g2.setFont(F_SMALL);g2.setColor(TEXT_DIM);g2.drawString(hint,12,getHeight()/2+5);g2.dispose();}}
        };
        f.setFont(F_BODY);f.setForeground(TEXT);f.setBackground(CARD2);f.setCaretColor(GOLD);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER,1,true),new EmptyBorder(10,12,10,12)));
        f.setMaximumSize(new Dimension(352,44)); f.setAlignmentX(CENTER_ALIGNMENT);
        f.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GOLD,1,true),new EmptyBorder(10,12,10,12)));}
            public void focusLost(FocusEvent e){f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER,1,true),new EmptyBorder(10,12,10,12)));}
        });
        return f;
    }

    private void stylePass(JPasswordField f){
        f.setFont(F_BODY);f.setForeground(TEXT);f.setBackground(CARD2);f.setCaretColor(GOLD);f.setEchoChar('●');
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER,1,true),new EmptyBorder(10,12,10,12)));
        f.setMaximumSize(new Dimension(352,44)); f.setAlignmentX(CENTER_ALIGNMENT);
        f.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GOLD,1,true),new EmptyBorder(10,12,10,12)));}
            public void focusLost(FocusEvent e){f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER,1,true),new EmptyBorder(10,12,10,12)));}
        });
    }

    private JTextField formField(String hint){
        JTextField f=loginField(hint); f.setMaximumSize(new Dimension(360,42)); f.setAlignmentX(LEFT_ALIGNMENT); return f;
    }
    private JTextField df(){
        JTextField f=new JTextField(); f.setFont(F_BODY);f.setForeground(TEXT);f.setBackground(CARD2);f.setCaretColor(GOLD);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER,1,true),new EmptyBorder(6,10,6,10)));
        return f;
    }

    private JButton goldBtn(String text){
        JButton btn=new JButton(text){
            boolean hov=false;
            {addMouseListener(new MouseAdapter(){public void mouseEntered(MouseEvent e){hov=true;repaint();}public void mouseExited(MouseEvent e){hov=false;repaint();}});}
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,hov?GOLD2:GOLD,getWidth(),getHeight(),hov?GOLD:GOLD_DIM));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10); g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(F_BTN); btn.setForeground(new Color(0x080E1A));
        btn.setBorderPainted(false);btn.setFocusPainted(false);btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); btn.setBorder(new EmptyBorder(12,28,12,28));
        return btn;
    }

    private JButton accentBtn(String text,Color color){
        JButton btn=new JButton(text){
            boolean hov=false;
            {addMouseListener(new MouseAdapter(){public void mouseEntered(MouseEvent e){hov=true;repaint();}public void mouseExited(MouseEvent e){hov=false;repaint();}});}
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                if(hov){g2.setColor(color);g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);setForeground(new Color(0x080E1A));}
                else{g2.setColor(CARD2);g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                    g2.setColor(color);g2.setStroke(new BasicStroke(1.2f));g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);setForeground(color);}
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI",Font.BOLD,11)); btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); btn.setBorder(new EmptyBorder(7,14,7,14));
        return btn;
    }

    private JButton pillBtn(String text,Color color){JButton b=accentBtn(text,color);b.setFont(F_SMALL);b.setBorder(new EmptyBorder(5,12,5,12));return b;}

    private JTable epicTable(DefaultTableModel model){
        JTable t=new JTable(model); t.setFont(F_BODY);t.setForeground(TEXT);t.setBackground(CARD);
        t.setGridColor(new Color(0x18243A)); t.setRowHeight(34);
        t.setSelectionBackground(new Color(0x162840)); t.setSelectionForeground(TEXT);
        t.setShowVerticalLines(false); t.setIntercellSpacing(new Dimension(0,1)); t.setFillsViewportHeight(true);
        JTableHeader h=t.getTableHeader(); h.setFont(F_SUB); h.setForeground(GOLD); h.setBackground(new Color(0x060C14));
        h.setDefaultRenderer(new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable tbl,Object v,boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(tbl,v,sel,foc,r,c);
                setBackground(new Color(0x060C14));setForeground(GOLD);setFont(F_SUB);
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,GOLD),new EmptyBorder(8,10,8,10)));
                return this;
            }
        });
        return t;
    }

    private JScrollPane scroll(JComponent comp){
        JScrollPane sp=new JScrollPane(comp); sp.setBackground(CARD); sp.getViewport().setBackground(CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER,1,true));
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            protected void configureScrollBarColors(){thumbColor=new Color(0x1E2D45);trackColor=CARD;}
            protected JButton createDecreaseButton(int o){JButton b=new JButton();b.setPreferredSize(new Dimension(0,0));return b;}
            protected JButton createIncreaseButton(int o){JButton b=new JButton();b.setPreferredSize(new Dimension(0,0));return b;}
        });
        return sp;
    }

    private JTextArea monoArea(){
        JTextArea a=new JTextArea(); a.setFont(F_MONO);a.setBackground(CARD);a.setForeground(TEXT);
        a.setEditable(false); a.setBorder(new EmptyBorder(14,18,14,18)); return a;
    }

    private JDialog mkDialog(String title,int w,int h){
        JDialog d=new JDialog(this,title,true); d.setSize(w,h); d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(CARD); d.setLayout(new BorderLayout());
        JLabel ttl=lbl("  "+title,F_HEAD,GOLD); ttl.setOpaque(true); ttl.setBackground(new Color(0x060C14));
        ttl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,GOLD),new EmptyBorder(14,10,14,10)));
        d.add(ttl,BorderLayout.NORTH); return d;
    }

    private void styleCombo(JComboBox<?> c){c.setFont(F_BODY);c.setBackground(CARD2);c.setForeground(TEXT);c.setBorder(BorderFactory.createLineBorder(BORDER,1,true));}

    private void confirmExit(){
        JDialog d=new JDialog(this,true);
        d.setUndecorated(true); d.setSize(500,260); d.setLocationRelativeTo(this);
        final boolean[] result={false};
        JPanel root=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,new Color(0x0E1A2A),0,getHeight(),new Color(0x090F1A)));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(AMBER);g2.setStroke(new BasicStroke(2f));g2.drawLine(50,0,getWidth()-50,0);
                g2.setColor(BORDER);g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.dispose();
            }
        };
        root.setOpaque(false); root.setBorder(new EmptyBorder(24,28,20,28));
        JPanel content=new JPanel(); content.setOpaque(false);
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        JLabel t1=lbl("Exit Application",new Font("Georgia",Font.BOLD,18),GOLD); t1.setAlignmentX(LEFT_ALIGNMENT);
        JLabel t2=lbl("Do you want to save all changes before exiting?",new Font("Segoe UI",Font.PLAIN,14),TEXT); t2.setAlignmentX(LEFT_ALIGNMENT);
        JLabel t3=lbl("Your inventory and sales data will be saved automatically.",F_SMALL,TEXT_DIM); t3.setAlignmentX(LEFT_ALIGNMENT);
        content.add(t1); content.add(Box.createVerticalStrut(12));
        content.add(t2); content.add(Box.createVerticalStrut(8));
        content.add(t3); content.add(Box.createVerticalStrut(28));
        JPanel btnRow=new JPanel(new FlowLayout(FlowLayout.CENTER,12,0)); btnRow.setOpaque(false);
        btnRow.setAlignmentX(LEFT_ALIGNMENT);
        JButton yesBtn=goldBtn("  Yes, Save & Exit  ");
        JButton noBtn=accentBtn("  Exit Without Saving  ",RED);
        JButton cancelBtn=accentBtn("  Cancel  ",TEXT_DIM);
        yesBtn.addActionListener(e->{result[0]=true;d.dispose();});
        noBtn.addActionListener(e->{result[0]=false;d.dispose();});
        cancelBtn.addActionListener(e->d.dispose());
        btnRow.add(cancelBtn); btnRow.add(noBtn); btnRow.add(yesBtn);
        content.add(btnRow);
        root.add(content,BorderLayout.CENTER);
        d.setContentPane(root); d.setVisible(true);
        if(result[0]){FileManager.saveInventory();FileManager.generateBackup();System.exit(0);}
        else if(!d.isVisible()&&!result[0]){
            // Check if cancel was pressed - do nothing, otherwise exit without save
        }
    }
}