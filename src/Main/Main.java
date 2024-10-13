package Main;


import FileManager.FileManager;
import User.RegisterUser;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        FileManager.createFile();

        System.out.println("Escolha uma opção de 1 a 5:");
        System.out.println(
                "1 - Cadastrar o usuário\n" +
                "2 - Listar todos usuários cadastrados\n" +
                "3 - Cadastrar nova pergunta no formulário\n" +
                "4 - Deletar pergunta do formulário\n" +
                "5 - Pesquisar usuário por nome, idade ou email\n");

        int opcao = s.nextInt();
        switch (opcao){
            case 1:
                FileManager.readFile();
                RegisterUser registerUser = new RegisterUser();
                break;
            case 2:
                FileManager.showAllFiles();
                break;
            case 3:
                FileManager.addOneQuestion();
                break;
            case 4:
                FileManager.deleteQuestionAdd();
                break;
            case 5:
                FileManager.getSearchFiles();
                break;
            default:
                    return;
        }

        s.close();
    }
}