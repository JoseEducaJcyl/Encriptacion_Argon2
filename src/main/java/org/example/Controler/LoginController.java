package org.example.Controler;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.DAO.UsuarioDAO;
import org.example.Security.PasswordService;
import org.example.DAO.UsuarioDAO.*;

import java.awt.event.ActionEvent;


public class LoginController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private final PasswordService passwordService =
            new PasswordService();
    @FXML
    private Button btnInsertar;
    @FXML
    private Button btnLogin;

    @FXML
    private Label lblMensaje;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;
    @FXML
    void onInsertar(javafx.event.ActionEvent actionEvent) {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {

            lblMensaje.setText(
                    "Completa todos los campos"
            );

            return;
        }

        String hash =
                passwordService.hashPassword(password);

        boolean registrado =
                usuarioDAO.registrar(email, hash);

        if (registrado) {

            lblMensaje.setText(
                    "Usuario registrado correctamente"
            );

            txtEmail.clear();
            txtPassword.clear();

        } else {

            lblMensaje.setText(
                    "No se pudo registrar el usuario"
            );
        }

    }

    @FXML
    public void onLogin(javafx.event.ActionEvent actionEvent) {
/*
Hay una cuestión que conviene corregir respecto a un ejemplo sencillo:
Argon2 es deliberadamente costoso
Para una aplicación JavaFX real, es mejor ejecutar el login en un Task:
Así JavaFX no bloquea el hilo de la interfaz mientras Argon2id realiza el cálculo.
¿Qué significa "ejecutarlo en un Task"?
Significa sacar el trabajo pesado del hilo de JavaFX y hacerlo en otro hilo.

Ejemplo completo
Supongamos que tienes un botón en tu FXML:

<Button
    text="Comenzar"
    onAction="#onComenzar"/>

Y tu controlador:

package org.example.Controler;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void onComenzar(ActionEvent event) {

        Task<String> task = new Task<>() {

            @Override
            protected String call() throws Exception {

                Thread.sleep(5000);

                return "Trabajo terminado";
            }
        };

        Thread hilo = new Thread(task);

        hilo.start();
    }
}

Cuando pulsas el botón:

Pulso "Comenzar"
       ↓
onComenzar()
       ↓
crea Task
       ↓
crea Thread
       ↓
hilo.start()
       ↓
┌─────────────────────────────┐
│ Thread secundario           │
│                             │
│ Thread.sleep(5000)          │
│                             │
│ Trabajo terminado           │
└─────────────────────────────┘

Mientras tanto...

┌─────────────────────────────┐
│ JavaFX Application Thread   │
│                             │
│ La interfaz sigue viva      │
│ Botones siguen funcionando  │
└─────────────────────────────┘

5. ¿Cómo sé cuándo termina?
Aquí está una de las partes más importantes.

Puedes hacer:

task.setOnSucceeded(event -> {

    String resultado = task.getValue();

    System.out.println(resultado);
});

El código completo:

@FXML
private void onComenzar(ActionEvent event) {

    Task<String> task = new Task<>() {

        @Override
        protected String call() throws Exception {

            Thread.sleep(5000);

            return "Trabajo terminado";
        }
    };

    task.setOnSucceeded(evento -> {

        String resultado = task.getValue();

        System.out.println(resultado);
    });

    Thread hilo = new Thread(task);

    hilo.start();
}

Aquí ocurre:

Task
 │
 │ call()
 │
 │ 5 segundos
 │
 ▼
"Trabajo terminado"
 │
 ▼
setOnSucceeded()
 │
 ▼
task.getValue()


 */
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
//obtiene el passwod almacenado en la BD
        Task<Boolean> task = new Task<>() {

            @Override
            protected Boolean call() {

                String hash =
                        usuarioDAO.obtenerPasswordHash(email);

                if (hash == null) {
                    return false;
                }

                return passwordService.verifyPassword(
                        password,
                        hash
                );
            }
        };

        task.setOnSucceeded(event -> {

            if (task.getValue()) {
                lblMensaje.setText("Login correcto");
            } else {
                lblMensaje.setText(
                        "Usuario o contraseña incorrectos"
                );
            }
        });

        new Thread(task).start();

    }//on login


}
