package User;

import FileManager.FileManager;

public class User {
    private final String nome;
    private final String email;
    private final int idade;
    private final double altura;

    protected User(String nome, int idade, double altura, String email) {
        this.nome = nome;
        this.idade = idade;
        this.altura = altura;
        this.email = email;

        FileManager.entryFile(nome,email,idade,altura);
    }

    public void showUser(){
        System.out.println("\nUsuário cadastrado: ");
        System.out.println(this.getNome());
        System.out.println(this.getEmail());
        System.out.println(this.getIdade());
        System.out.println(this.getAltura());
    }

    public double getAltura() {
        return altura;
    }

    public int getIdade() {
        return idade;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }
}
