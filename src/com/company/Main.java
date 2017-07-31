package com.company;

//Khryshcheniuk Pavlo



import java.util.*;

public class Main {

    public static class Vertex {

        public int number;
        public int biconnected_part;
        public int[] biconnected_parts_list;
        public int last_biconnected;
        public boolean articulation;
        public boolean was_visited;
        public int adj_list[];
        public int contemporary_edge;
        public int last_edge;


        Vertex(int num, int n) {
            last_biconnected = 0;
            this.number = num;
            was_visited = false;
            articulation = false;
            adj_list = new int[n + 1];
            contemporary_edge = 0;
            last_edge = 0;
            biconnected_parts_list = new int[n + 1];
            clean_list(n);
        }

        void add_edge(int to_vertex) {
            adj_list[last_edge] = to_vertex;
            last_edge++;

        }

        int next_edge() {
            contemporary_edge++;
            return adj_list[contemporary_edge - 1];
        }

        void clean_list(int n) {
            for (int i = 0; i < n + 1; i++) {
                adj_list[i] = -1;
                biconnected_parts_list[i] = -1;
            }
        }

        boolean is_in_biconnected(int x) {
            for (int i = 0; i < last_biconnected; i++)
                if (biconnected_parts_list[i] == x)
                    return true;
            return false;
        }

        void add_biconnected(int id) {
            if (last_biconnected == 0 || biconnected_parts_list[last_biconnected - 1] != id) {
                biconnected_parts_list[last_biconnected] = id;
                last_biconnected++;

            }
        }


    }

    static class Graph {
        int n;
        Vertex[] vertex_list;
        public static int[][] l_matrix;
        public static int l;
        public static int articulation;
        public static int children_root;
        public static int biconnected;
        public static int pk1;
        public static int pk2;
        public static int bridges;


        public static int root;
        public static boolean boo;


        Graph(int n) {
            boo = true;
            root = -1;
            pk1 = pk2 = l = bridges = biconnected = articulation = children_root = 0;
            this.n = n;
            vertex_list = new Vertex[n];
            for (int i = 0; i < n; i++)
                vertex_list[i] = new Vertex(i, n);
        }


        public void output() {

            System.out.println(pk1);
            for (int i = 1; i <= pk1; i++) {
                int j = 0;
                while (vertex_list[j].biconnected_part != i) {
                    j++;
                }
                System.out.print(j + 1);
                for (int m = j + 1; m < n; m++)
                    if (vertex_list[m].biconnected_part == i)
                        System.out.print("," + (m + 1));
                System.out.println();
            }
            System.out.println(biconnected);
            for (int i = 0; i < pk2; i++) {
                if (l_matrix[i][0] != 3) {
                    for (int j = 1; j < l_matrix[i][0] - 1; j++) {
                        System.out.print(l_matrix[i][j] + ",");
                    }
                    System.out.print(l_matrix[i][l_matrix[i][0] - 1]);
                    System.out.println();
                }

            }
            System.out.println(bridges);
            int z = 0;
            while (z < pk2 && l_matrix[z][0] != 3) {
                z++;
            }
            if (z != pk2)
                System.out.print(l_matrix[z][1] + "-" + l_matrix[z][2]);
            for (int i = z + 1; i < pk2; i++) {
                if (l_matrix[i][0] == 3)
                    System.out.print("," + l_matrix[i][1] + "-" + l_matrix[i][2]);
            }
            if (bridges != 0)
                System.out.println();
            System.out.println(articulation);
            z = 0;
            while (z < n && !vertex_list[z].articulation)
                z++;
            if (z != n)
                System.out.print(z + 1);
            for (int i = z + 1; i < n; i++) {
                if (vertex_list[i].articulation)
                    System.out.print("," + (i + 1));
            }
            if (articulation != 0)
                System.out.println();


        }

        public void inputGraph(Scanner inScan) {
            for (int i = 0; i < n; i++) {
                int m = inScan.nextInt();
                for (int j = 0; j < m; j++)
                    vertex_list[i].add_edge(inScan.nextInt() - 1);
            }
        }

        public void sort() {
            l_matrix = new int[pk2][n + 1];

            for (int i = 0; i < pk2; i++) {

                int g = 1;
                for (int j = 0; j < n; j++) {
                    if (vertex_list[j].is_in_biconnected(i + 1)) {
                        l_matrix[i][g] = j + 1;
                        g++;

                    }
                }
                l_matrix[i][0] = g;
            }

            for (int i = 0; i < pk2; i++) {
                int min = i;
                for (int j = i + 1; j < pk2; j++) {
                    if (compare(l_matrix[j], l_matrix[min]))
                        min = j;
                }
                int temp[] = l_matrix[i];
                l_matrix[i] = l_matrix[min];
                l_matrix[min] = temp;
            }

        }

        public void search(int v, int u, int[] nr, int[] low, Stack<Integer> S1, Stack<Integer> S2) {

            vertex_list[v].was_visited = true;
            vertex_list[v].biconnected_part = pk1;
            l++;
            nr[v] = l;
            low[v] = l;
            int w = vertex_list[v].next_edge();
            while (w != -1) {
                if (!vertex_list[w].was_visited) {
                    if (u == -1) {
                        children_root++;
                        if (children_root == 2 && boo) {
                            if (vertex_list[v].articulation == false) {
                                articulation++;
                                vertex_list[v].articulation = true;
                            }
                        }
                    }
                    S1.push(v);
                    S2.push(w);
                    search(w, v, nr, low, S1, S2);
                    if (low[w] >= nr[v]) {
                        pk2++;
                        int x, y, i = 0;
                        if (u != -1) {
                            if (vertex_list[v].articulation == false) {
                                articulation++;
                                vertex_list[v].articulation = true;
                            }
                        }

                        do {
                            i++;
                            x = S1.pop();
                            y = S2.pop();

                            vertex_list[x].add_biconnected(pk2);

                            vertex_list[y].add_biconnected(pk2);

                        } while (!(v == x && w == y));
                        if (i == 1)
                            bridges++;
                        else
                            biconnected++;

                    } else if (low[w] < low[v]) low[v] = low[w];
                } else if (nr[w] < nr[v] && w != u) {
                    S1.push(v);
                    S2.push(w);
                    if (nr[w] < low[v]) low[v] = nr[w];
                }
                w = vertex_list[v].next_edge();
            }
        }


        public void str_search() {
            int[] num = new int[n];
            int[] low = new int[n];
            Stack<Integer> Stos1 = new Stack<>();
            Stack<Integer> Stos2 = new Stack<>();
            int current = 0;
            pk2 = 0;
            pk1 = 0;
            root = current;
            while (current != n) {
                pk1++;
                children_root = 0;
                boo = true;

                search(current, -1, num, low, Stos1, Stos2);

                while (current < n && vertex_list[current].was_visited) {
                    current++;
                }


            }
        }

        public boolean compare(int[] tab1, int[] tab2) {
            int i = 1;
            while (tab1[i] == tab2[i])
                i++;
            return (tab1[i] < tab2[i]);


        }


    }

    public static void main(String[] args) {
        Scanner inScan = new Scanner(System.in);
        int p = inScan.nextInt();
        for (int i = 0; i < p; i++) {
            int n = inScan.nextInt();
            Graph graph;
            graph = new Graph(n);
            graph.inputGraph(inScan);
            graph.str_search();
            graph.sort();
            graph.output();


        }


    }


}

/*
3
7
2 2 7
4 1 3 6 7
3 2 4 5
2 3 5
2 3 4
2 2 7
3 1 2 6
4
1 4
1 4
0
2 1 2
19
1 13
2 4 19
2 16 5
2 2 18
2 16 3
2 10 11
1 12
0
1 18
3 11 12 6
2 6 10
5 7 14 10 15 17
1 1
3 12 17 15
3 12 17 14
4 3 5 18 19
3 15 14 12
3 4 9 16
2 2 16
*/