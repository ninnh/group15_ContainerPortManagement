package Assignment3.AdminManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Exception;

//create LoginForm class to create login form
//class extends JFrame to create a window where our component add
//class implements ActionListener to perform an action on button click
class LoginForm extends JFrame implements ActionListener
{
    //initialize button, panel, label, and text field
    JButton button;
    JPanel Panel;
    JLabel username, password;
    final JTextField  textField1, textField2;

    //calling constructor
    LoginForm()
    {
        //create label for username
        username = new JLabel();
        username.setText("Username");      //set label value for textField1

        //create text field to get username from the user
        textField1 = new JTextField(15);    //set length of the text

        //create label for password
        password = new JLabel();
        password.setText("Password");      //set label value for textField2

        //create text field to get password from the user
        textField2 = new JPasswordField(15);    //set length for the password

        //create submit button
        button = new JButton("SUBMIT"); //set label to button

        //create panel to put form elements
        Panel = new JPanel(new GridLayout(3, 1));
        Panel.add(username);    //set username label to panel
        Panel.add(textField1);   //set text field to panel
        Panel.add(password);    //set password label to panel
        Panel.add(textField2);   //set text field to panel
        Panel.add(button);           //set button to panel

        //set border to panel
        add(Panel, BorderLayout.CENTER);

        //perform action on button click
        button.addActionListener(this);     //add action listener to button
        setTitle("LOGIN FORM");         //set title to the login form
    }

    //define abstract method actionPerformed() which will be called on button click
    public void actionPerformed(ActionEvent ae)     //pass action listener as a parameter
    {
        String userValue = textField1.getText();        //get user entered username from the textField1
        String passValue = textField2.getText();        //get user entered password from the textField2

        //check whether the credentials are authentic or not
        //check admin credentials
        if (userValue.equals("admin@gmail.com") && passValue.equals("admin")) {
            //if authentic, navigate user to a new page
            //create instance of the NewPage
            NewPage page = new NewPage();

            //make page visible to the user
            page.setVisible(true);

            //create a welcome label and set it to the new page
            JLabel wel_label = new JLabel("Welcome: " + userValue);
            page.getContentPane().add(wel_label);
        } else if (userValue.equals("admin@gmail.com") && passValue != "admin") {
            System.out.println("Password is not correct. Try again.");
        } else if (userValue != "admin@gmail.com" && passValue.equals("admin")) {
            System.out.println("Username is not correct. Try again.");
        }
        //check manager credentials
        else if (userValue.equals("manager@gmail.com") && passValue.equals("manager")) {  //if authentic, navigate user to a new page

            //create instance of the NewPage
            NewPage page = new NewPage();

            //make page visible to the user
            page.setVisible(true);

            //create a welcome label and set it to the new page
            JLabel wel_label = new JLabel("Welcome: " + userValue);
            page.getContentPane().add(wel_label);
        } else if (userValue.equals("manager@gmail.com") && passValue != "manager") {
            System.out.println("Password is not correct. Try again.");
        } else if (userValue != "manager@gmail.com" && passValue.equals("manager")) {
            System.out.println("Username is not correct. Try again.");
        }else {
            //show error message
            System.out.println("Please enter valid username and password");
        }
    }
}

//create the main class
class Admin
{
    //main() method start
    public static void main(String arg[])
    {
        try
        {
            //create instance of the LoginForm
            LoginForm form = new LoginForm();
            form.setSize(300,125);  //set size of the frame
            form.setVisible(true);  //make form visible to the user
        }
        catch(Exception e)
        {
            //handle exception
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
/*
- Admin
User name: admin@gmail.com
Password: admin

- Manager
User name: manager@gmail.com
Password: manager
*/
