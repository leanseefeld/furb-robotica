package br.furb.robotica.estruturas;

/**
 * Criada com o objetivo de poder testar os algorítmos sem vinculo com a biblioteca do Lejos
 * @author Gustavo
 *
 * @param <T>
 */
public interface MinhaPilha<T> {
    public void empilhar(T objeto);
    public T pegar();
    public boolean estaVazia();
}
