import java.util.Scanner;
import java.util.Random;
public class Main {

    static int MAXIMO_TEMPO_EXECUCAO = 65535;
    static int n_processos = 3; //é preciso definir o número de processos pois ele será o número de posições do vetor

    public static void main(String[] args) {

        int[] tempo_execucao = new int[n_processos];
        int[] tempo_chegada = new int[n_processos];
        int[] prioridade = new int[n_processos];
        int[] tempo_espera = new int[n_processos];
        int[] tempo_restante = new int[n_processos]; //vetor auxiliar para armazenar o tempo restante de cada processo ==vai decrementando do tempo de execução


        Scanner teclado = new Scanner (System.in);


        popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        //Escolher algoritmo
        int alg;

        while(true) {
            System.out.println();
            System.out.println("Menu:");
            System.out.println();
            System.out.println ("1 = Algoritmo FCFS");
            System.out.println ("2 = Algoritmo SJF Preemptivo");
            System.out.println ("3 = Algoritmo SJF Não Preemptivo");
            System.out.println ("4 = Algoritmo Prioridade Preemptivo");
            System.out.println ("5 = Algoritmo Prioridade Não Preemptivo");
            System.out.println ("6 = Algoritmo Round_Robin");
            System.out.println ("7 = Imprime lista de processos");
            System.out.println ("8 = Popular processos novamente");
            System.out.println ("9 = Sair");
            System.out.println();

            System.out.println("Escolha um número entre as opções do menu:");
            alg =  teclado.nextInt();


            if (alg == 1) { //FCFS
                FCFS(tempo_execucao, tempo_espera, tempo_restante);
            }
            else if (alg == 2) { //SJF PREEMPTIVO
                SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            }
            else if (alg == 3) { //SJF NÃO PREEMPTIVO
                SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);

            }
            else if (alg == 4) { //PRIORIDADE PREEMPTIVO
                PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 5) { //PRIORIDADE NÃO PREEMPTIVO
                PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

            }
            else if (alg == 6) { //Round_Robin
                Round_Robin(tempo_execucao, tempo_espera, tempo_restante);

            }
            else if (alg == 7) { //IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 8) { //REATRIBUI VALORES INICIAIS
                popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 9) {
                break;

            }
        }
    }

    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int [] prioridade ){
        Random random = new Random();
        Scanner teclado = new Scanner (System.in);
        int aleatorio;

        System.out.println("A população de processos erá aleatória? 1 = sim, 2 = não");

        aleatorio =  teclado.nextInt();

        for (int i = 0; i < n_processos; i++) {
            //Popular Processos Aleatorio
            if (aleatorio == 1){
                tempo_execucao[i] = random.nextInt(10)+1; //10 é o limite máximo, coloco +1 porque não quero que o tempo seja zero
                tempo_chegada[i] = random.nextInt(10)+1;
                prioridade[i] = random.nextInt(15)+1;
            }
            //Popular Processos Manual
            else {
                System.out.print("Digite o tempo de execução do processo["+i+"]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo["+i+"]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo["+i+"]:  ");
                prioridade[i] = teclado.nextInt();
            }
            tempo_restante[i] = tempo_execucao[i];
        }
    }

    public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int []prioridade){
        //Imprime lista de processos
        for (int i = 0; i < n_processos; i++) {
            System.out.println("Processo["+i+"]: tempo_execucao="+ tempo_execucao[i] + " tempo_restante="+tempo_restante[i] + " tempo_chegada=" + tempo_chegada[i] + " prioridade =" +prioridade[i]);
        }
    }

    public static void imprime_stats (int[] espera) {   //Implementar o calculo e impressão de estatisticas
        int[] tempo_espera = espera.clone();
        double tempo_espera_total = 0;

        for (int i=0; i<n_processos; i++){
            System.out.println("Processo ["+i+"]: tempo_espera="+tempo_espera[i]);
            tempo_espera_total += tempo_espera[i];
        }
        System.out.println("Tempo médio de espera: "+(tempo_espera_total/n_processos));
    }

    public static void FCFS(int[] execucao, int[] espera, int[] restante){
        //clones dos parâmetros de entrada, para não alterar os valores do vetor inicial
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int processo_em_execucao = 0; //processo inicial no FCFS é sempre zero

        //implementar código do FCFS:
        for (int i=0; i<MAXIMO_TEMPO_EXECUCAO; i++){
            System.out.println ("tempo["+i+"]: processo["+processo_em_execucao+"] restante="+tempo_restante[processo_em_execucao]);

            if (tempo_execucao[processo_em_execucao]==tempo_restante [processo_em_execucao]){ //acabou de começar a ser executado
                tempo_espera[processo_em_execucao] = i-1; //tempo anterior
            }

            if (tempo_restante [processo_em_execucao] ==1){ //processo foi concluído
                if (processo_em_execucao == (n_processos-1)){ //se o processo é o último, finaliza algoritmo
                    break;
                }
                else{
                    processo_em_execucao++; //se não for o último, avança para o próximo processo
                }
            }
            else{
                tempo_restante [processo_em_execucao]--; //o tempo restante do processo em execução é reduzido em uma unidade""
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        //implementar código do SJF preemptivo e não preemptivo
        //...
        //

        imprime_stats(tempo_espera);

    }

    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridade_temp = prioridade.clone();

        //implementar código do Prioridade preemptivo e não preemptivo
        //...
        //

        imprime_stats(tempo_espera);

    }

    public static void Round_Robin(int[] execucao, int[] espera, int[] restante){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();


        //implementar código do Round-Robin
        //...
        //

        imprime_stats(tempo_espera);
    }
}