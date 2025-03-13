package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

import com.jtattoo.plaf.smart.SmartLookAndFeel; // Choose any theme

//import Design.Connection;

public class Inscription extends JFrame {

	private JFrame frame;
	private JTextField login;
	private JPasswordField mdp;
	private JPasswordField mdpConfirmer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Set JTattoo Look and Feel
		            UIManager.setLookAndFeel(new SmartLookAndFeel());
					Inscription window = new Inscription();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param password
	 * @return
	 */
	
	
	// Méthode pour valider le mot de passe
    static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+<>?]).{12,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    /**
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


    
    
    
   
	/**
	 * Create the application.
	 * @return 
	 * @return 
	 */
	public  Inscription() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 572, 386);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		login = new JTextField();
		login.setBounds(329, 64, 119, 26);
		login.setColumns(10);
		
		JButton buttonInscription = new JButton("Valider");
		buttonInscription.setBounds(450, 294, 87, 29);
		buttonInscription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// recuperation des données du formulaire
				String loginText = login.getText();
				String mdpText = mdp.getText();
				String mdpConfirmText = mdpConfirmer.getText();

 
				// verifier si le login et le mdp sont valides
				if (loginText.isEmpty() || mdpText.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Veuillez remplir tous different les  champs");
					return; // Sortir de la méthode si les champs sont vides
				}
				
				
				// verifier si le format de l'email est valide
				if (!isValidEmail(loginText)) {
	                JOptionPane.showMessageDialog(frame, "Format email invalide. Utilisez le format exemple@domaine.com", "Erreur", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
				
				
				 // Vérification de la robustesse du mot de passe
                if (!isValidPassword(mdpText)) {
                    JOptionPane.showMessageDialog(null, 
                        "Le mot de passe doit contenir :\n- Au moins 12 caractères\n- Une majuscule\n- Une minuscule\n- Un chiffre\n- Un caractère spécial (!@#$%^&*()-_+=<>?)",
                        "Mot de passe invalide", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Verifier si les mot de passe apres confirmation correspondent
                if (!mdpText.equals(mdpConfirmText)) {
                    JOptionPane.showMessageDialog(frame, "Les mots de passe ne correspondent pas", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

 
				// Demander confirmation
				int confirmation = JOptionPane.showConfirmDialog(
						frame,
						"Voulez-vous vraiment enregistrer ces informations ?",
						"Confirmation",
						JOptionPane.YES_NO_OPTION);
 
				if (confirmation == JOptionPane.YES_OPTION) {
					try {
						// Créer le dossier Auth s'il n'existe pas
						File directory = new File("src");
						directory.mkdirs();
 
						// Créer ou écraser le fichier save.txt
						File file = new File("src/save.txt");
						FileWriter writer = new FileWriter(file, true);
						writer.write("login: " + loginText + "\n");
						writer.write("mdp: " + hashPassword(mdpText) + "\n");
						writer.close();
 
						// Afficher un message de succès
						JOptionPane.showMessageDialog(
								frame,
								"Inscription réussie !",
								"Succès",
								JOptionPane.INFORMATION_MESSAGE);
						
						//frame.dispose();
						//Connection window = new Connection(); // Ouvre la fenetre connexion
							
						
						
						
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(
								frame,
								"Erreur lors de l'enregistrement : " + ex.getMessage(),
								"Erreur",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("Login:");
		lblNewLabel.setBounds(217, 69, 78, 16);
		
		JLabel lblNewLabel_1 = new JLabel("Mot de passe:");
		lblNewLabel_1.setBounds(187, 121, 93, 16);
		
		JLabel lblNewLabel_1_1 = new JLabel("Inscrivez-vous");
		lblNewLabel_1_1.setBounds(450, 266, 105, 16);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Inscription");
		lblNewLabel_1_1_1.setBounds(271, 6, 105, 16);
		
		mdp = new JPasswordField();
		mdp.setBounds(329, 116, 119, 26);
		
		mdpConfirmer = new JPasswordField();
		mdpConfirmer.setBounds(329, 167, 119, 26);
		
		JLabel lblNewLabel_1_2 = new JLabel("Confirmer le Mot de passe:");
		lblNewLabel_1_2.setBounds(126, 172, 169, 16);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(mdpConfirmer);
		frame.getContentPane().add(lblNewLabel_1_2);
		frame.getContentPane().add(lblNewLabel_1_1);
		frame.getContentPane().add(buttonInscription);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(lblNewLabel_1);
		frame.getContentPane().add(mdp);
		frame.getContentPane().add(login);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("Vous avez deja un identifiant? Connectez-vous");
		lblNewLabel_2.setBounds(6, 266, 303, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				Connection window = new Connection();
				window.afficher();
				
			}
		});
		btnConnect.setBounds(84, 294, 87, 29);
		frame.getContentPane().add(btnConnect);
	}
	


public void afficher() {
	frame.setVisible(true);
}


    }



