package User;

import java.util.Scanner;

public class RegisterUser {
    public RegisterUser(){
        Scanner s = new Scanner(System.in);

        String nome = s.nextLine();
        String email = s.nextLine();
        int idade = s.nextInt();
        double altura = s.nextDouble();

        User user = new User(nome,idade,altura,email);

        user.showUser();
    }
}
