import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    static int MAXIMO_TEMPO_EXECUCAO = 65535;
    static ArrayList<Processo> n_processos = new ArrayList<>(3);


    public static void main(String[] args) {


        Scanner teclado = new Scanner(System.in);

        popular_processos();

        imprime_processos();

        //Escolher algoritmo
        int alg;
        boolean continuarExecucao = true;

        while (continuarExecucao) {
            System.out.println();
            System.out.println("**********************************");
            System.out.println("            Menu:");
            System.out.println();
            System.out.println("1 = Algoritmo FCFS");
            System.out.println("2 = Algoritmo SJF Preemptivo");
            System.out.println("3 = Algoritmo SJF Não Preemptivo");
            System.out.println("4 = Algoritmo Prioridade Preemptivo");
            System.out.println("5 = Algoritmo Prioridade Não Preemptivo");
            System.out.println("6 = Algoritmo Round_Robin");
            System.out.println("7 = Imprime lista de processos");
            System.out.println("8 = Popular processos novamente");
            System.out.println("9 = Sair");
            System.out.println();

            System.out.println("Escolha um número entre as opções do menu:");
            alg = teclado.nextInt();

            switch (alg) {
                case 1:
                    FCFS();
                    break;
                case 2:
                    SJF(true);
                    break;
                case 3:
                    SJF(false);
                    break;
                case 4:
                    PRIORIDADE(true);
                    break;
                case 5:
                    PRIORIDADE(false);
                    break;
                case 6:
                    Round_Robin();
                    break;
                case 7:
                    imprime_processos();
                    break;
                case 8:
                    popular_processos();
                    imprime_processos();
                    break;
                case 9:
                    continuarExecucao = false;
                    System.out.print("Até logo!");
                    break;
                default:
                    System.out.print("Opção inválida. Digite um número entre 1 e 9");
            }
        }
    }

    public static void popular_processos() {
        Random random = new Random();
        Scanner teclado = new Scanner(System.in);
        int aleatorio;

        System.out.println("A população de processos erá aleatória? 1 = sim, 2 = não");

        aleatorio = teclado.nextInt();

        for (int i = 0; i < 3; i++) {
            //Popular Processos Aleatorio
            if (aleatorio == 1) {
                n_processos.add(new Processo(random.nextInt(10) + 1, random.nextInt(10) + 1, random.nextInt(15) + 1));
            }
            //Popular Processos Manual
            else {
                System.out.print("Digite o tempo de execução do processo[" + i + "]:  ");
                n_processos.get(i).tempo_execucao = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo[" + i + "]:  ");
                n_processos.get(i).tempo_chegada = teclado.nextInt();
                System.out.print("Digite a prioridade do processo[" + i + "]:  ");
                n_processos.get(i).prioridade = teclado.nextInt();
            }
            n_processos.get(i).tempo_restante = n_processos.get(i).tempo_execucao;
        }
    }

    public static void imprime_processos() {
        //Imprime lista de processos
        for (int i = 0; i < n_processos.size(); i++) {
            System.out.println("Processo[" + i + "]: tempo_execucao=" + n_processos.get(i).tempo_execucao + " tempo_restante=" + n_processos.get(i).tempo_restante + " tempo_chegada=" + n_processos.get(i).tempo_chegada + " prioridade =" + n_processos.get(i).prioridade);
        }
    }

    public static void imprime_stats(ArrayList<Processo> cloneProcessos) {   //Implementar o calculo e impressão de estatisticas
        double tempo_espera_total = 0;

        for (int i = 0; i < cloneProcessos.size(); i++) {
            System.out.println("Processo [" + i + "]: tempo_espera=" + cloneProcessos.get(i).tempo_espera);
            tempo_espera_total += cloneProcessos.get(i).tempo_espera;
        }
        System.out.println("Tempo médio de espera: " + (tempo_espera_total / cloneProcessos.size()));
    }

    public static void FCFS() {
        //clones dos parâmetros de entrada, para não alterar os valores do vetor inicial
        System.out.println("Execução do algoritmo FCFS: ");
        System.out.println();
        ArrayList <Processo> cloneProcessos = (ArrayList)n_processos.clone(); //fiz um clone do Arraylist de processos

        int processo_em_execucao = 0; //processo inicial no FCFS é sempre zero

        //implementar código do FCFS:

        for (int i = 1; i < MAXIMO_TEMPO_EXECUCAO; i++) {
            System.out.println("tempo[" + i + "]: processo[" + processo_em_execucao + "] restante=" + cloneProcessos.get(processo_em_execucao).tempo_restante);

            if (cloneProcessos.get(processo_em_execucao).tempo_execucao == cloneProcessos.get(processo_em_execucao).tempo_restante) { //acabou de começar a ser executado
                cloneProcessos.get(processo_em_execucao).tempo_espera = i - 1; //tempo anterior
            }

            if (cloneProcessos.get(processo_em_execucao).tempo_restante == 1) { //processo foi concluído
                if (processo_em_execucao == (cloneProcessos.size() - 1)) { //se o processo é o último, finaliza algoritmo
                    break;
                } else {
                    processo_em_execucao++; //se não for o último, avança para o próximo processo
                }
            } else {
                cloneProcessos.get(processo_em_execucao).tempo_restante--; //o tempo restante do processo em execução é reduzido em uma unidade""
            }
        }
        System.out.println();
        System.out.println("Estatísticas do tempo de espera:");
        System.out.println();
        imprime_stats(cloneProcessos);
    }

    public static void SJF(boolean preemptivo) {

        ArrayList <Processo> cloneProcessos2 = (ArrayList)n_processos.clone(); //fiz um clone do Arraylist de processos

        int processos_restantes = cloneProcessos2.size();

        for (int tempo_atual = 1; processos_restantes > 0; tempo_atual++) { //faz a contagem do tempo

            int menor_tempo_execucao = MAXIMO_TEMPO_EXECUCAO;
            int indice_processo_menor_tempo = -1;
            int tempo_inicio_execucao = 0;


            if (preemptivo) {
                //verifica se o processo já chegou encontra o processo com o menor tempo de execução (calculado pelo tempo restante)
                for (int k = 0; k < cloneProcessos2.size(); k++) { //percorre a lista de processos
                    if (cloneProcessos2.get(k).tempo_chegada <= tempo_atual && cloneProcessos2.get(k).tempo_restante < menor_tempo_execucao && cloneProcessos2.get(k).tempo_restante > 0) { //verifica  se o processo já chegou
                        menor_tempo_execucao = cloneProcessos2.get(k).tempo_restante;
                        indice_processo_menor_tempo = k;
                    }
                }
            }else{ //não-preemptivo
                //verifica o processo que já chegou e com menor tempo execução
                for (int k = 0; k < cloneProcessos2.size(); k++) { //percorre a lista de processos
                    if (cloneProcessos2.get(k).tempo_chegada <= tempo_atual && cloneProcessos2.get(k).tempo_execucao < menor_tempo_execucao && cloneProcessos2.get(k).tempo_restante > 0) { //verifica  se o processo já chegou
                        menor_tempo_execucao = cloneProcessos2.get(k).tempo_execucao;
                        indice_processo_menor_tempo = k;
                    }
                }
            }

            //se o primeiro processo a ser processado ainda não chegou
            if (indice_processo_menor_tempo == -1) {
                System.out.println("tempo[" + tempo_atual + "]: nenhum processo está pronto");

            } else {

                int tempo_execucao_processo = cloneProcessos2.get(indice_processo_menor_tempo).tempo_execucao;
                int tempo_restante_processo = cloneProcessos2.get(indice_processo_menor_tempo).tempo_restante;
                int tempo_espera_processo = cloneProcessos2.get(indice_processo_menor_tempo).tempo_espera;
                int tempo_chegada_processo = cloneProcessos2.get(indice_processo_menor_tempo).tempo_chegada;

                // Executa o processo com menor tempo de execução e diminui uma unidade de tempo restante
                tempo_restante_processo--;
                System.out.println("tempo[" + tempo_atual + "]: processo[" + indice_processo_menor_tempo + "] restante=" + tempo_restante_processo);
                cloneProcessos2.get(indice_processo_menor_tempo).tempo_restante = tempo_restante_processo;


                // calcular o tempo de espera
                if (tempo_execucao_processo == tempo_restante_processo){ //quando o processo começa a ser processado (para o sjf não preemptivo)
                    tempo_espera_processo = tempo_atual - tempo_chegada_processo;
                    tempo_inicio_execucao = tempo_espera_processo; //guardei o resultado em uma variável para utilizar no cálculo do preemptivo
                }if (tempo_restante_processo < tempo_execucao_processo){ //quando já houve processamento de uma parte do processo
                    tempo_espera_processo = tempo_atual - tempo_inicio_execucao - (tempo_execucao_processo - tempo_restante_processo);
                }

                cloneProcessos2.get(indice_processo_menor_tempo).tempo_espera = tempo_espera_processo > 0 ? tempo_espera_processo : 0;

                // Verifica se o processo foi concluído, diminui os processos restantes e passa para o próximo
                if (tempo_restante_processo == 0) {
                    processos_restantes--;
                }
            }
        }
        System.out.println();
        System.out.println("Estatísticas do tempo de espera:");

        imprime_stats(cloneProcessos2);
    }

    public static void PRIORIDADE(boolean preemptivo){
        //implementar código do Prioridade preemptivo e não preemptivo
        //..

        // imprime_stats(tempo_espera);

    }
    public static void Round_Robin(){
        //implementar código do Round-Robin
        //...
        //
        //imprime_stats(tempo_espera);
    }
}
