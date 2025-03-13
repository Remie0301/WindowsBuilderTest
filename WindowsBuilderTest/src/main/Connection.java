package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

import javax.swing.*;
import java.util.Properties;
import com.jtattoo.plaf.smart.SmartLookAndFeel; // Choose any theme

//import Design.Inscription;

@SuppressWarnings("unused")
public class Connection extends JFrame {

	private JFrame frame;
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	
	
	/**
	 * Cette Methode permet de valider les emails
	 * 
	 * 
	 * 
	 * @param email
	 * @return
	 */
	
	 // Methode pour valider l'email
    static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
	
    
 // Method to hash password using SHA-256
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    
    
    //}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

		            // Set JTattoo Look and Feel
		            UIManager.setLookAndFeel(new SmartLookAndFeel());
					Connection window = new Connection();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Connection() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 603, 405);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		usernameField = new JTextField();
		usernameField.setBounds(317, 103, 130, 26);
		frame.getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		JButton btnNewButton = new JButton("Valider");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				String loginText = usernameField.getText();
		        @SuppressWarnings("deprecation")
				String mdpText = passwordField.getText();

		        // Vérifier si le login et le mot de passe sont valides
		        
		        if (loginText.isEmpty() || mdpText.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs");
		            
		            return; // Sortir de la méthode si les champs sont vides
		        }

		        // Demander confirmation
		        int confirmation = JOptionPane.showConfirmDialog(
		                frame,
		                "Voulez-vous vraiment vous connecter avec ces informations ?",
		                "Confirmation",
		                JOptionPane.YES_NO_OPTION);

		        if (confirmation == JOptionPane.YES_OPTION) {
		            try {
		                // Créer ou ouvrir le fichier "save.txt" pour vérifier si le login existe
		                File file = new File("src/save.txt");
		                if (!file.exists()) {
		                    // Si le fichier n'existe pas encore, on ne peut pas se connecter
		                    JOptionPane.showMessageDialog(frame, "Aucun utilisateur trouvé. Veuillez d'abord vous inscrire.");
		                    return;
		                }

		                // Lire le fichier et vérifier si les informations existent
		                BufferedReader reader = new BufferedReader(new FileReader(file));
		                String line;
		                String savedLogin = "";
		                String savedHashedPassword = "";
		                boolean found = false;

		                // Lire le fichier ligne par ligne
		                while ((line = reader.readLine()) != null) {
		                    if (line.startsWith("login:")) {
		                        savedLogin = line.split(":")[1].trim();
		                    } else if (line.startsWith("mdp:")) {
		                        savedHashedPassword = line.split(":")[1].trim();
		                        
		                        if (!isValidEmail(loginText)) {
		        	                JOptionPane.showMessageDialog(frame, "Format email invalide. Utilisez le format exemple@domaine.com", "Erreur", JOptionPane.ERROR_MESSAGE);
		        	                return;
		        	            }
		        				
		                        
		                     // Hash input password and compare with stored hash
		                       // String hashedInputPassword = hashPassword(mdpText);
		                        if (savedLogin.equals(loginText) && savedHashedPassword.equals(hashPassword(mdpText))) {
		                            found = true;
		                            break;
		                        }		                    }
		                }
		                reader.close();

		                // Vérifier si le login et le mot de passe correspondent
		                if (savedLogin.equals(loginText) && savedHashedPassword.equals(hashPassword(mdpText))) {
		                    JOptionPane.showMessageDialog(frame, "Connexion réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
		                } else {
		                    JOptionPane.showMessageDialog(frame, "Login ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
		                }

		            } catch (IOException ex) {
		                JOptionPane.showMessageDialog(frame, "Erreur lors de la lecture du fichier : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		                ex.printStackTrace();
		            }
		        }
		    }
	
			
			
		});
		btnNewButton.setBounds(443, 313, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setBounds(230, 108, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Mot de passe");
		lblNewLabel_1.setBounds(198, 194, 93, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnAnnuler = new JButton("Inscription");
		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Inscription inscription = new Inscription();
				frame.dispose(); // Fermer la fenêtre de connexion
				inscription.afficher(); // Utiliser la nouvelle méthode
			
				
				
				
			}
			
			
			
		});
		btnAnnuler.setBounds(22, 313, 117, 29);
		frame.getContentPane().add(btnAnnuler);
		
		JLabel lblNewLabel_2 = new JLabel("Connectez-vous");
		lblNewLabel_2.setBounds(237, 16, 210, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(317, 189, 130, 26);
		frame.getContentPane().add(passwordField);
		
		JLabel lblNewLabel_3 = new JLabel("Vous n'avez pas d'identifiant ? Inscrivez-vous ");
		lblNewLabel_3.setBounds(6, 276, 304, 16);
		frame.getContentPane().add(lblNewLabel_3);
	}
	
	public void afficher() {

		frame.setVisible(true);
}
}
