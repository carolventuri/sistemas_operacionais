public class Processo {
    int tempo_chegada;
    int tempo_execucao;
    int tempo_espera;
    int tempo_restante;
    int prioridade;
    int id;


    public Processo (int tempo_chegada, int tempo_execucao, int prioridade) {
        this.tempo_chegada = tempo_chegada;
        this.tempo_execucao = tempo_execucao;
        this.prioridade = prioridade;
        this.tempo_restante = tempo_execucao;
    }

    public void setid(int z) {
        this.id = z;
    }
}
