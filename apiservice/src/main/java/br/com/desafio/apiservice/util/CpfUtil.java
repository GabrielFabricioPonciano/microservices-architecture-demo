package br.com.desafio.apiservice.util;

public class CpfUtil {
    public static String normalizar(String cpf){
        if (cpf == null){
            return "";
        }
        return cpf.replaceAll("\\D","");
    }
    public static boolean isValido(String cpf){
        return normalizar(cpf).length() == 11;
    }
}
