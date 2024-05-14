import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    static int MAXIMO_TEMPO_EXECUCAO = 655;

    static ArrayList<Processo> n_processos = new ArrayList<>(3);

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        popular_processos();

        imprime_processos();

        //Escolher algoritmo
        int alg;
        boolean continuarExecucao = true;

        while (continuarExecucao) {

            System.out.println ("\n**********************************\n" +
                    "            Menu:\n\n" +
                    "1 = Algoritmo FCFS\n" +
                    "2 = Algoritmo SJF Preemptivo\n" +
                    "3 = Algoritmo SJF Não Preemptivo\n" +
                    "4 = Algoritmo Prioridade Preemptivo\n" +
                    "5 = Algoritmo Prioridade Não Preemptivo\n" +
                    "6 = Algoritmo Round_Robin\n" +
                    "7 = Imprime lista de processos\n" +
                    "8 = Popular processos novamente\n" +
                    "9 = Sair\n\n");

            System.out.println("Escolha um número entre as opções do menu:");
            alg = teclado.nextInt();

            switch (alg) {
                case 1:
                    System.out.println("Execução do algoritmo FCFS: ");
                    System.out.println();
                    FCFS();
                    break;
                case 2:
                    System.out.println("Execução do algoritmo SJF Preemptivo: ");
                    System.out.println();
                    SJF(true);
                    break;
                case 3:
                    System.out.println("Execução do algoritmo SJF Não-Preemptivo: ");
                    System.out.println();
                    SJF(false);
                    break;
                case 4:
                    System.out.println("Execução do algoritmo Prioridade Preemptivo: ");
                    System.out.println();
                    PRIORIDADE(true);
                    break;
                case 5:
                    System.out.println("Execução do algoritmo Prioridade Não-Preemptivo: ");
                    System.out.println();
                    PRIORIDADE(false);
                    break;
                case 6:
                    System.out.println("Execução do algoritmo Round Robin: ");
                    System.out.println();
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
        n_processos.clear();
        Random random = new Random();
        Scanner teclado = new Scanner(System.in);
        int aleatorio;

        System.out.println("A população de processos erá aleatória? 1 = sim, 2 = não");

        aleatorio = teclado.nextInt();


        //Popular Processos Aleatorio
        if (aleatorio == 1) {
            for (int i = 0; i < 3; i++){
                n_processos.add(new Processo(random.nextInt(10) + 1, random.nextInt(10) + 1, random.nextInt(15) + 1));
                n_processos.get(i).setid(i);
            }
        }
        //Popular Processos Manual
        else {
            for (int i = 0; i < 3; i++) {
                System.out.print("Digite o tempo de chegada do processo[" + i + "]:  ");
                int tempo_chegada = teclado.nextInt();
                System.out.print("Digite o tempo de execução do processo[" + i + "]:  ");
                int tempo_execucao = teclado.nextInt();
                System.out.print("Digite a prioridade do processo[" + i + "]:  ");
                int prioridade = teclado.nextInt();

                n_processos.add(new Processo(tempo_chegada, tempo_execucao, prioridade));
                n_processos.get(i).setid(i);
            }
        }
    }

    public static void imprime_processos() {
        //Imprime lista de processos
        for (int i = 0; i < n_processos.size(); i++) {
            System.out.println("Processo[" + n_processos.get(i).id + "]: tempo_execucao=" + n_processos.get(i).tempo_execucao + " tempo_restante=" + n_processos.get(i).tempo_restante + " tempo_chegada=" + n_processos.get(i).tempo_chegada + " prioridade =" + n_processos.get(i).prioridade);
        }
    }

    public static void imprime_stats(ArrayList<Processo> cloneProcessos) {   //Implementar o calculo e impressão de estatisticas
        double tempo_espera_total = 0;

        for (int i = 0; i < cloneProcessos.size(); i++) {
            System.out.println("Processo [" + cloneProcessos.get(i).id + "]: tempo_espera=" + cloneProcessos.get(i).tempo_espera);
            tempo_espera_total += cloneProcessos.get(i).tempo_espera;
        }
        System.out.println("Tempo médio de espera: " + (tempo_espera_total / cloneProcessos.size()));
    }

    public static void FCFS() {
        //clones dos parâmetros de entrada, para não alterar os valores do vetor inicial
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

        //restabelecer o tempo restante = tempo execução para o próximo algoritmo que será executado
        for (Processo nProcesso : n_processos) {
            nProcesso.tempo_restante = nProcesso.tempo_execucao;
        }
    }

    public static void SJF(boolean preemptivo) {

        ArrayList <Processo> cloneProcessos2 = (ArrayList)n_processos.clone(); //fiz um clone do Arraylist de processos

        int processos_restantes = n_processos.size();
        boolean processo_em_execucao = false;
        int indice_processo_execucao = 0;
        boolean chegou_processo = true;
        int ultimoprocesso = -1;

        for (int tempo_atual = 1; processos_restantes > 0; tempo_atual++) { //faz a contagem do tempo

            int menor_tempo_execucao = MAXIMO_TEMPO_EXECUCAO;

            if (preemptivo) {
                //verifica se o processo já chegou encontra o processo com o menor tempo de execução (calculado pelo tempo restante)
                for (int k = 0; k < cloneProcessos2.size(); k++) { //percorre a lista de processos
                    if (cloneProcessos2.get(k).tempo_chegada <= tempo_atual && cloneProcessos2.get(k).tempo_restante < menor_tempo_execucao && cloneProcessos2.get(k).tempo_restante > 0) { //verifica  se o processo já chegou
                        menor_tempo_execucao = cloneProcessos2.get(k).tempo_restante;
                        indice_processo_execucao = k;
                        processo_em_execucao = true;
                    }
                }
            }else{ //não-preemptivo
                // ordenar os processos por menor tempo de chegada
                for (int k = 0; k < cloneProcessos2.size() - 1; k++) { //percorre a lista de processos
                    for (int l = 0; l < cloneProcessos2.size() - k - 1; l++) {
                        if (cloneProcessos2.get(l).tempo_chegada > cloneProcessos2.get(l + 1).tempo_chegada) {
                            Processo temp = cloneProcessos2.get(l);
                            cloneProcessos2.set(l, cloneProcessos2.get(l + 1));
                            cloneProcessos2.set(l + 1, temp);
                        }
                    }
                }
                //se o tempo de chegada é igual, ordena por menor tempo de execução
                for (int k = 0; k < cloneProcessos2.size() - 1; k++) { //percorre a lista de processos
                    for (int l = 0; l < cloneProcessos2.size() - k - 1; l++) {
                        if (cloneProcessos2.get(l).tempo_chegada == cloneProcessos2.get(l + 1).tempo_chegada && cloneProcessos2.get(l).tempo_execucao > cloneProcessos2.get(l + 1).tempo_execucao) {
                            Processo temp = cloneProcessos2.get(l);
                            cloneProcessos2.set(l, cloneProcessos2.get(l + 1));
                            cloneProcessos2.set(l + 1, temp);
                        }
                    }
                }
                //executa o primeiro processo que chegou (inicializei o indice_processo_execucao = 0)
                if (cloneProcessos2.get(indice_processo_execucao).tempo_chegada <= tempo_atual && cloneProcessos2.get(indice_processo_execucao).tempo_restante > 0 ){
                    processo_em_execucao = true;
                }
                //verificar o processo que tem menor tempo de execução, quando o processo anterior foi executado
                for (int k = 0; k < cloneProcessos2.size(); k++) { //percorre a lista de processos
                    if (cloneProcessos2.get(k).tempo_chegada <= tempo_atual && cloneProcessos2.get(k).tempo_restante > 0 && cloneProcessos2.get(indice_processo_execucao).tempo_restante == 0) {
                        if (cloneProcessos2.get(k).tempo_execucao < menor_tempo_execucao){
                            menor_tempo_execucao = cloneProcessos2.get(k).tempo_execucao;
                            indice_processo_execucao = k;
                            processo_em_execucao = true;
                        }
                    }
                }
            }

            //se o primeiro processo a ser processado ainda não chegou
            if (processo_em_execucao == false) {
                System.out.println("tempo[" + tempo_atual + "]: nenhum processo está pronto");

            } else {
                // Executa o processo com menor tempo de execução e diminui uma unidade de tempo restante
                System.out.println("tempo[" + tempo_atual + "]: processo[" + cloneProcessos2.get(indice_processo_execucao).id + "] restante=" + cloneProcessos2.get(indice_processo_execucao).tempo_restante);

                // calcular o tempo de espera
                int tempo_espera_processo = cloneProcessos2.get(indice_processo_execucao).tempo_espera;

                if (chegou_processo ||ultimoprocesso!=cloneProcessos2.get(indice_processo_execucao).id){
                    tempo_espera_processo = (tempo_atual - cloneProcessos2.get(indice_processo_execucao).tempo_chegada) - (cloneProcessos2.get(indice_processo_execucao).tempo_execucao-cloneProcessos2.get(indice_processo_execucao).tempo_restante);
                }

                cloneProcessos2.get(indice_processo_execucao).tempo_espera = tempo_espera_processo > 0 ? tempo_espera_processo : 0;

                chegou_processo = false;
                processo_em_execucao = false;
                ultimoprocesso =  cloneProcessos2.get(indice_processo_execucao).id;
                cloneProcessos2.get(indice_processo_execucao).tempo_restante -= 1;

                // Verifica se o processo foi concluído, diminui os processos restantes e passa para o próximo
                if (cloneProcessos2.get(indice_processo_execucao).tempo_restante == 0) {
                    processos_restantes--;
                    chegou_processo = true;
                }
            }
        }
        System.out.println();
        System.out.println("Estatísticas do tempo de espera:");

        imprime_stats(cloneProcessos2);
        //restabelecer o tempo restante = tempo execução para o próximo algoritmo que será executado
        for (Processo nProcesso : n_processos) {
            nProcesso.tempo_restante = nProcesso.tempo_execucao;
        }
    }

    public static void PRIORIDADE(boolean preemptivo) {
        ArrayList<Processo> cloneProcessos3 = (ArrayList) n_processos.clone(); //fiz um clone do Arraylist de processos

        int processos_restantes = cloneProcessos3.size();

        for (int tempo_atual = 1; processos_restantes > 0; tempo_atual++) { //faz a contagem do tempo

            int maior_prioridade = 0;
            int indice_processo_execucao = 0;
            boolean chegou_processo = true;
            int ultimoprocesso = -1;
            boolean processo_em_execucao = false;

            //verifica se o processo já chegou e encontra o processo com a maior prioridade
            if (preemptivo){
                for (int k = 0; k < cloneProcessos3.size(); k++) { //percorre a lista de processos
                    if (cloneProcessos3.get(k).tempo_chegada <= tempo_atual && cloneProcessos3.get(k).prioridade > maior_prioridade && cloneProcessos3.get(k).tempo_restante > 0) { //verifica  se o processo já chegou
                        maior_prioridade = cloneProcessos3.get(k).prioridade;
                        indice_processo_execucao = k;
                        processo_em_execucao = true;
                    }
                }
            }else { //não preemptivo

                //ordenar os processos por menor tempo de chegada
                for (int k = 0; k < cloneProcessos3.size() - 1; k++) { //percorre a lista de processos
                    for (int l = 0; l < cloneProcessos3.size() - k - 1; l++) {
                        if (cloneProcessos3.get(l).tempo_chegada > cloneProcessos3.get(l + 1).tempo_chegada) {
                            Processo temp = cloneProcessos3.get(l);
                            cloneProcessos3.set(l, cloneProcessos3.get(l + 1));
                            cloneProcessos3.set(l + 1, temp);
                        }
                    }
                }
                //se o tempo de chegada é igual, ordena por maior prioridade
                for (int k = 0; k < cloneProcessos3.size() - 1; k++) { //percorre a lista de processos
                    for (int l = 0; l < cloneProcessos3.size() - k - 1; l++) {
                        if (cloneProcessos3.get(l).tempo_chegada == cloneProcessos3.get(l + 1).tempo_chegada && cloneProcessos3.get(l).prioridade < cloneProcessos3.get(l + 1).prioridade) {
                            Processo temp = cloneProcessos3.get(l);
                            cloneProcessos3.set(l, cloneProcessos3.get(l + 1));
                            cloneProcessos3.set(l + 1, temp);
                        }
                    }
                }
                //executa o primeiro processo que chegou (inicializei o indice_processo_execucao = 0)
                if (cloneProcessos3.get(indice_processo_execucao).tempo_chegada <= tempo_atual && cloneProcessos3.get(indice_processo_execucao).tempo_restante > 0 ){
                    processo_em_execucao = true;
                }
                //verificar o processo que tem maior prioridade, quando o processo anterior foi executado
                for (int k = 0; k < cloneProcessos3.size(); k++) { //percorre a lista de processos
                    if (cloneProcessos3.get(k).tempo_chegada <= tempo_atual && cloneProcessos3.get(k).tempo_restante > 0 && cloneProcessos3.get(indice_processo_execucao).tempo_restante == 0) {
                        if (cloneProcessos3.get(k).prioridade>maior_prioridade){
                            maior_prioridade = cloneProcessos3.get(k).prioridade;
                            indice_processo_execucao = k;
                            processo_em_execucao = true;
                        }
                    }
                }
            }
            //se o primeiro processo a ser processado ainda não chegou
            if (processo_em_execucao == false) {
                System.out.println("tempo[" + tempo_atual + "]: nenhum processo está pronto");

            } else {
                // Executa o processo com maior prioridade
                System.out.println("tempo[" + tempo_atual + "]: processo[" + cloneProcessos3.get(indice_processo_execucao).id + "] restante=" + cloneProcessos3.get(indice_processo_execucao).tempo_restante);

                // calcular o tempo de espera
                int tempo_espera_processo = cloneProcessos3.get(indice_processo_execucao).tempo_espera;

                if (chegou_processo || ultimoprocesso!=cloneProcessos3.get(indice_processo_execucao).id){
                    tempo_espera_processo = (tempo_atual - cloneProcessos3.get(indice_processo_execucao).tempo_chegada) - (cloneProcessos3.get(indice_processo_execucao).tempo_execucao-cloneProcessos3.get(indice_processo_execucao).tempo_restante);
                }

                cloneProcessos3.get(indice_processo_execucao).tempo_espera = tempo_espera_processo > 0 ? tempo_espera_processo : 0;

                chegou_processo = false;
                processo_em_execucao = false;
                ultimoprocesso =  cloneProcessos3.get(indice_processo_execucao).id;
                cloneProcessos3.get(indice_processo_execucao).tempo_restante -= 1;

                // Verifica se o processo foi concluído, diminui os processos restantes e passa para o próximo
                if (cloneProcessos3.get(indice_processo_execucao).tempo_restante == 0) {
                    processos_restantes--;
                    chegou_processo = true;
                }
            }
        }
        System.out.println();
        System.out.println("Estatísticas do tempo de espera:");

        imprime_stats(cloneProcessos3);

        //restabelecer o tempo restante = tempo execução para o próximo algoritmo que será executado
        for (Processo nProcesso : n_processos) {
            nProcesso.tempo_restante = nProcesso.tempo_execucao;
        }
    }

    public static void Round_Robin() {

        Scanner teclado = new Scanner(System.in);

        ArrayList<Processo> cloneProcessos4 = (ArrayList) n_processos.clone(); //fiz um clone do Arraylist de processos

        System.out.println("Digite o timeslice: ");
        int timeslice = teclado.nextInt();


        int tempo_atual = 1;
        int processos_restantes = cloneProcessos4.size();

        while (processos_restantes > 0) {

            for (int k = 0; k < cloneProcessos4.size(); k++) {

                int t_processamento = timeslice;

                while (cloneProcessos4.get(k).tempo_restante > 0 && t_processamento > 0) {
                    System.out.println("tempo[" + tempo_atual + "]: processo[" + cloneProcessos4.get(k).id + "] restante=" + cloneProcessos4.get(k).tempo_restante);
                    cloneProcessos4.get(k).tempo_restante--;
                    t_processamento--;
                    tempo_atual++;

                    if (cloneProcessos4.get(k).tempo_restante == 0) { // Verifica se o processo foi concluído, diminui os processos restantes
                        processos_restantes--;
                    }
                }
            }
        }
        //restabelecer o tempo restante = tempo execução para o próximo algoritmo que será executado
        for (Processo nProcesso : n_processos) {
            nProcesso.tempo_restante = nProcesso.tempo_execucao;
        }
    }
}
