package com.hdlg.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    // Método para copiar propriedades não nulas de um objeto de origem para um objeto de destino.
    public static void copyNonNullProperties(Object source, Object target){
        // Utiliza a classe BeanUtils do Spring para copiar propriedades.
        // O terceiro argumento é um array de nomes de propriedades a serem ignoradas durante a cópia.
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
    
    // Método para obter os nomes das propriedades nulas em um objeto.
    public static String[] getNullPropertyNames(Object source){
        // Cria um BeanWrapper a partir do objeto de origem.
        final BeanWrapper src = new BeanWrapperImpl(source);
        
        // Obtém as propriedades do objeto.
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        
        // Cria um conjunto para armazenar os nomes de propriedades nulas.
        Set<String> emptyNames = new HashSet<>();
        
        // Itera sobre as propriedades.
        for (var pd : pds) {
            // Obtém o valor da propriedade no objeto de origem.
            Object srcValue = src.getPropertyValue(pd.getName());
            // Se o valor for nulo, adiciona o nome da propriedade ao conjunto de nomes vazios.
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        
        // Converte o conjunto em um array de strings.
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
