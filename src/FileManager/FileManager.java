package FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {
    private static File createDirectory(String string) {
        File fileDirectory = new File(string);
        fileDirectory.mkdir();
        return fileDirectory;
    }

    public static File createFile() {
        File fileDir = createDirectory("src\\file");
        File file = new File(fileDir, "arquivo.txt");
        if (file.exists()) {
            return file;
        } else {
            try (FileWriter fileWriter = new FileWriter(file);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write("Responda o questionário na seguinte ordem:");
                bufferedWriter.newLine();
                bufferedWriter.write("1-Qual seu nome completo?");
                bufferedWriter.newLine();
                bufferedWriter.write("2-Qual seu email de contato?");
                bufferedWriter.newLine();
                bufferedWriter.write("3-Qual sua idade?");
                bufferedWriter.newLine();
                bufferedWriter.write("4-Qual sua altura?");
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    public static void readFile() {
        try (FileReader fileReader = new FileReader(createFile());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void entryFile(String nome, String email, int idade, double altura) {
        File fileDirectory = createDirectory("src\\file\\Users");
        File[] allFiles = fileDirectory.listFiles();
        File file = new File(fileDirectory, nome.trim().toUpperCase().replace(" ", "") + ".txt");
        Scanner s = new Scanner(System.in);

        String nameValidation = "[\\D]+";
        String emailValidation = "([\\w])+@([\\w])+";

        Pattern patternName = Pattern.compile(nameValidation);
        Matcher matcherName = patternName.matcher(nome);
        if (!matcherName.find()) {
            throw new RuntimeException("seu nome não deverá conter digitos ou caracteres especiais");
        }
        if (nome.length() < 10) {
            throw new RuntimeException("seu nome deve conter no mínimo 10 digitos");
        }

        Pattern patternEmail = Pattern.compile(emailValidation);
        Matcher matcherEmail = patternEmail.matcher(email);
        if (!matcherEmail.find()) {
            throw new RuntimeException("email invalido");
        }
        for (File fileUser : allFiles) {
            try (Scanner scanner = new Scanner(fileUser)) {
                scanner.nextLine();
                String line = scanner.nextLine();
                if (line.substring(2).equals(email)) {
                    throw new RuntimeException("email já utilizado");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (idade < 18) {
            throw new RuntimeException("você não possui a idade necessária");
        }

        if (!String.valueOf(altura).contains(".")) {
            throw new RuntimeException("diga sua altura exata, utilizando ',' ");
        }


        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("1-" + nome);
            bufferedWriter.newLine();
            bufferedWriter.write("2-" + email);
            bufferedWriter.newLine();
            bufferedWriter.write("3-" + idade);
            bufferedWriter.newLine();
            bufferedWriter.write("4-" + altura);
            bufferedWriter.newLine();
            for (int i = 5; i < readLines(); i++) {
                bufferedWriter.write(i + "-" + s.nextLine());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showAllFiles() {
        File fileDirectory = createDirectory("src\\file\\Users");
        File[] files = fileDirectory.listFiles();
        int i = 1;

        for (File file : files) {
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line1 = bufferedReader.readLine();
                String[] nome = line1.split("-");
                System.out.println(i + "-" + nome[1].trim());
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int readLines() {
        int i = 0;

        try (FileReader fileReader = new FileReader(createFile());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    public static void addOneQuestion() {
        File file = createFile();
        File fileTemp = new File("src\\file\\temp.txt");
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int lineNumber = 0;

        System.out.println("digite a pergunta que você quer adicionar:");
        String lineExtra = "-" + scanner.nextLine();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            lines.add(lineExtra);
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileTemp))) {
            for (String line : lines) {
                if (4 < lineNumber && !Character.isDigit(line.charAt(0))) {
                    bufferedWriter.write(String.valueOf(lineNumber));
                }
                bufferedWriter.write(line.trim());
                bufferedWriter.newLine();
                bufferedWriter.flush();
                lineNumber++;
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.delete()) {
            fileTemp.renameTo(file);
        } else {
            throw new RuntimeException("Não foi possível fazer a alteração do arquivo!");
        }
    }

    public static void deleteQuestionAdd() {
        Scanner s = new Scanner(System.in);
        File file = createFile();
        File fileTemp = new File("src\\file\\temp.txt");
        System.out.println("qual o número da linha que você quer deletar? (válido apenas a linha 5 ou maior)");
        int l = s.nextInt();
        int actualLine = 0;

        if (l < 5) throw new RuntimeException("Não é possível alterar esta linha.");
        else if (l > readLines()) throw new RuntimeException("Essa linha não existe.");
        else {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileTemp))) {
                String line;
                int numberLine = 1;
                while ((line = bufferedReader.readLine()) != null) {
                    if (l != actualLine) {
                        if (Character.isDigit(line.charAt(0))) {
                            bufferedWriter.write(String.valueOf(numberLine));
                            bufferedWriter.write(line.substring(1));
                            numberLine++;
                        } else {
                            bufferedWriter.write(line);
                        }
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    actualLine++;
                }
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.delete()) {
                fileTemp.renameTo(file);
                System.out.println("linha deletada com sucesso!");
            } else {
                throw new RuntimeException("Erro ao deletar o arquivo original");
            }
        }
    }

    public static void getSearchFiles() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("pesquise por nome, email ou idade: ");
        String search = scanner.nextLine();

        File fileDir = createDirectory("src\\file\\Users");
        File[] allFiles = fileDir.listFiles();
        List<File> searchFiles = new ArrayList<>();

        for (File file : allFiles) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.toUpperCase().contains(search.toUpperCase())) {
                        searchFiles.add(file);
                        break;
                    }
                }
                if (searchFiles.isEmpty()) {
                    throw new RuntimeException("não foi encontrado nenhum arquivo com esse dado");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File file : searchFiles) {
            try {
                Scanner scannerFile = new Scanner(file);
                System.out.println(file.getName());
                while (scannerFile.hasNext()) {
                    System.out.println(scannerFile.nextLine());
                }
                System.out.println("\n");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
                                                                                                                                                                                                