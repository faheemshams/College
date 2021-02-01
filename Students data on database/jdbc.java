import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

class jdbc extends JFrame implements ActionListener
{  
   JPanel pMain, pEnter, pView;

   //MAIN PANEL
   JButton bEnter,bView;
   JLabel lHeader;
    
   //DATA ENTERING PANEL
   JLabel lName,lRoll,lPhone,lKtuid;
   JTextField tName,tRoll,tPhone,tKtuid;
   JButton bSubmit,bBack;

   //DATA VIEW PANEL
   JScrollPane scrollBar;
   JButton bDone;
   JTable table;
   DefaultTableModel dtm;

   //DATABASE OBJECTS
   Connection con=null;
   Statement stmt=null;
   
   jdbc()
   { 
      try                                                   
      {  
        try
        {
           Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {  System.out.println("deploy mySql j connector jar file into CLASSPATH");}
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root","3002");
        stmt=con.createStatement();
        stmt.execute("create database if not exists student");
        stmt.execute("use student");
        stmt.execute("create table if not exists student_table(roll int(3),name varchar(30),phone varchar(15),ktu_id varchar(15),primary key(roll))");
      }
      catch(Exception e)
        {  
           JOptionPane.showMessageDialog(this,"ERROR IN ACCESSING DATABASE","",JOptionPane.WARNING_MESSAGE);
        }

    //FRAME PART
      setTitle("ASSIGNMENT 5");
      setSize(500,500);
      setLayout(null);
      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
 
      pMain=new JPanel();
      pEnter=new JPanel();
      pView=new JPanel();
      add(pMain);
      add(pEnter);
      add(pView);

    //MAIN PANEL COMPONENTS
      pMain.setLayout(null);
      bEnter=new JButton("ENTER DATA");
      bView=new JButton("VIEW DATA");
      lHeader=new JLabel("STUDENTS DATA");
      
      pMain.setBounds(0,0,500,500);
      lHeader.setBounds(180,120,150,40);
      bEnter.setBounds(160,200,150,30);
      bView.setBounds(160,250,150,30);

      pMain.add(lHeader);
      pMain.add(bEnter);
      pMain.add(bView);
      
      bEnter.addActionListener(this);
      bView.addActionListener(this);
      bEnter.setFocusable(false);                    
      bView.setFocusable(false);
      
    //ENTER PANEL COMPONENTS PART
      pEnter.setLayout(null);
      pEnter.setBounds(0,0,500,500);
      pEnter.setVisible(false);

      lName=new JLabel("NAME - ");
      lRoll=new JLabel("ROLL NUMBER -");
      lPhone=new JLabel("MOBILE NUMBER -");   
      lKtuid=new JLabel("KTU ID -");
      tName=new JTextField();
      tRoll=new JTextField();
      tPhone=new JTextField();
      tKtuid=new JTextField();
      bBack=new JButton("BACK");
      bSubmit=new JButton("SUBMIT");

      lName.setBounds(40,80,150,30);    tName.setBounds(200,80,250,30);
      lRoll.setBounds(40,140,150,30);   tRoll.setBounds(200,140,250,30);
      lPhone.setBounds(40,200,150,30);  tPhone.setBounds(200,200,250,30);
      lKtuid.setBounds(40,260,150,30);  tKtuid.setBounds(200,260,250,30);
      bBack.setBounds(120,350,100,30);   bSubmit.setBounds(270,350,100,30);
      
      pEnter.add(lName);   pEnter.add(tName);
      pEnter.add(lRoll);   pEnter.add(tRoll);
      pEnter.add(lPhone);  pEnter.add(tPhone);
      pEnter.add(lKtuid);  pEnter.add(tKtuid);
      pEnter.add(bBack);   pEnter.add(bSubmit);

      bBack.addActionListener(this);
      bSubmit.addActionListener(this);
      bBack.setFocusable(false);
      bSubmit.setFocusable(false);

    //VIEW PANEL COMPONENTS
      pView.setLayout(null);
      pView.setVisible(false);
      bDone=new JButton("DONE");
      
      table=new JTable();
      dtm=new DefaultTableModel(0,0);
      String header[]=new String[]{"Roll","name","phone","ktuid"}; 
      dtm.setColumnIdentifiers(header);
      table.setModel(dtm);
      scrollBar=new JScrollPane(table);
      
      pView.setBounds(0,0,500,500);
      bDone.setBounds(210,400,80,30);
      scrollBar.setBounds(50,30,400,350);

      pView.add(scrollBar);
      pView.add(bDone);

      bDone.addActionListener(this);
      bDone.setFocusable(false);

    //To close connections to database while existing program
       addWindowListener(new WindowAdapter()       
       {
          public void windowClosing(WindowEvent e)
          {
            try{stmt.close();} catch(Exception ex){System.out.println("error while closing statement"); }
            try{con.close();}  catch(Exception ex){System.out.println("error while closing connection");}
            dispose();
          }}); 
    }

   
   public void actionPerformed(ActionEvent e)
   {
     if(e.getSource()==bEnter)
     {  
        pMain.setVisible(false);
        pEnter.setVisible(true);
     }
     else if(e.getSource()==bBack)
     {
        pMain.setVisible(true);
        pEnter.setVisible(false);
     }
     else if(e.getSource()==bSubmit)
     {
        pEnter.setVisible(false);
        pMain.setVisible(true);
        dataBase(1);
     }
     else if(e.getSource()==bView)
     {
        pMain.setVisible(false);
        pView.setVisible(true);
        dataBase(2);
     }
     else if(e.getSource()==bDone)
     {
        pView.setVisible(false);
        pMain.setVisible(true);
     }
   }
   void dataBase(int mode)
   {
    if(mode==1)                                                    //enter data
    {
      if(tName.getText().equals(""))
      {
        JOptionPane.showMessageDialog(this,"NAME NOT ENTERED","",JOptionPane.WARNING_MESSAGE);
        clearText();
        return;
      }
      try
        {
           int roll=Integer.parseInt(tRoll.getText());
           stmt.executeUpdate("insert into student_table values("+roll+",'"+tName.getText()+"','"+tPhone.getText()+"','"+tKtuid.getText()+"')");
        }                    
      catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"ROLL ALREADY EXISTS/INVALID","ERROR",JOptionPane.WARNING_MESSAGE);
        }
    }
    else if(mode==2)                                                 //view data
    {  
       int rowCount = dtm.getRowCount();              
       for (int i=rowCount-1;i>=0;i--) 
       dtm.removeRow(i);
       try
       {
       ResultSet rs=stmt.executeQuery("select * from student_table");
       while(rs.next())
       {Object data[]={rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)};
       dtm.addRow(data);}
       }
       catch(Exception e)
       {
        JOptionPane.showMessageDialog(this,"NOTHING TO DISPLAY","EMPTY",JOptionPane.WARNING_MESSAGE);
       }
    }
    mode=0;
    clearText();
   }
   void clearText()
   {
      tName.setText("");
      tRoll.setText("");
      tPhone.setText("");
      tKtuid.setText("");
   }
public static void main(String args[])
   {
      new jdbc();     
   }}
