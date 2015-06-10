package util;

/**
 * Criada com o objetivo de poder testar os algorítmos sem vinculo com a biblioteca do Lejos
 * 
 * @author Gustavo
 * @param <T>
 */
public interface Pilha<T> {

    public void empilhar(T objeto);

    public boolean estaVazia();

    public T pegar();
}
