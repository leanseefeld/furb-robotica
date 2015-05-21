package br.furb.robotica;

/**
 * Criada com o objetivo de poder testar os algorítmos sem vinculo com a biblioteca do Lejos
 * @author Gustavo
 *
 * @param <T>
 */
public interface MinhaPilha<T> {
    public void colocar(T objeto);
    public T pegar();
    public boolean estaVazia();
}
