import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

// @SuppressWarnings
class Graph {
    public int n = -1; // 顶点数
    public int m = -1; // 边数
    public ArrayList<int[]>[] adjList = null;// 初始化邻接表

    public Graph() {
    }

    @SuppressWarnings("unchecked")
    public Graph(int n, int m) {
        this.n = n;
        this.m = m;
        this.adjList = new ArrayList[n + 1];
    }
}

class AGV {
    public int start = -1;
    public int end = -1;
    public int cur = -1;

    public AGV(int s, int e) {
        start = s;
        end = e;
        cur = s;
    }
}

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        try {
            // 读取输入文件
            Scanner scanner = new Scanner(new File("data1.txt"));
            int n = scanner.nextInt(); // 顶点数
            int m = scanner.nextInt(); // 边数
            graph = new Graph(n, m);
            for (int i = 0; i <= graph.n; i++) {
                graph.adjList[i] = new ArrayList<>();
            }
            // 读入边信息，建立邻接表
            for (int i = 0; i < graph.m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                graph.adjList[u].add(new int[] { v, w });
                graph.adjList[v].add(new int[] { u, w });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 输入起点和终点
        Scanner input = new Scanner(System.in);
        System.out.print("请输入AGV1起点：");
        int start1 = input.nextInt();
        System.out.print("请输入AGV1终点：");
        int end1 = input.nextInt();
        System.out.print("请输入AGV2起点：");
        int start2 = input.nextInt();
        System.out.print("请输入AGV2终点：");
        int end2 = input.nextInt();
        input.close();
        AGV agv1 = new AGV(start1, end1);
        AGV agv2 = new AGV(start2, end2);
        int time = 0;
        int dist1 = 0;
        int dist2 = 0;
        int cur1;
        int cur2;
        int next1;
        int next2;
        ArrayList<Integer> agvPath1 = dijkstra(graph, agv1);
        ArrayList<Integer> agvPath2 = dijkstra(graph, agv2);
        while (agv1.cur != agv1.end || agv2.cur != agv2.end) {
            time++;
            // 超时退出
            if (time > 100)
                break;
            // AGV1无路径且未到达终点
            if (agvPath1 == null && agv1.cur != agv1.end) {
                System.out.println("AGV1 无路径");
                break;
            } else if (agv1.cur == agv1.end) {
                cur1 = agv1.end;
                next1 = -1;
            } else {
                cur1 = agvPath1.get(0);
                next1 = agvPath1.get(1);
            }
            // AGV2无路径且未到达终点
            if (agvPath2 == null && agv2.cur != agv2.end) {
                System.out.println("AGV2 无路径");
                break;
            } else if (agv2.cur == agv2.end) {
                cur2 = agv2.end;
                next2 = -1;
            } else {
                cur2 = agvPath2.get(0);
                next2 = agvPath2.get(1);
            }
            System.out.printf("第 %5d 分钟:AGV1在%5d   下一点%5d   | AGV2在%5d   下一点%5d  \n", time, cur1, next1, cur2, next2);
            if (next1 == next2) {
                // 默认AGV2等待AGV1先行
                System.out.println("AGV1与AGV2路径冲突，AGV2等待：");
                dist1 += getDist(graph, agv1.cur, next1);
                agv1.cur = next1;
                agvPath1 = dijkstra(graph, agv1);
            } else {
                if (next1 != -1) {
                    dist1 += getDist(graph, agv1.cur, next1);
                    agv1.cur = next1;
                    agvPath1 = dijkstra(graph, agv1);
                }
                if (next2 != -1) {
                    dist2 += getDist(graph, agv2.cur, next2);
                    agv2.cur = next2;
                    agvPath2 = dijkstra(graph, agv2);
                }
            }
        }
        System.out.println("========\n总用时 " + ++time + " 分钟");
        System.out.println("AGV1路程：" + dist1 + "；AGV2路程：" + dist2);
    }

    public static ArrayList<Integer> dijkstra(Graph g, AGV agv) {
        // Dijkstra算法求最短路径
        int[] dist = new int[g.n + 1]; // 存放起点到各点的距离
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[agv.cur] = 0;
        int[] prev = new int[g.n + 1]; // 存放到达每个节点的前一个节点
        Arrays.fill(prev, -1);
        boolean[] visited = new boolean[g.n + 1];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // 小根堆
        pq.offer(new int[] { agv.cur, 0 });
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            if (visited[u])
                continue;
            visited[u] = true;
            for (int[] edge : g.adjList[u]) {
                int v = edge[0];
                int w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    prev[v] = u;
                    pq.offer(new int[] { v, dist[v] });
                }
            }
        }

        // 输出最短路径
        if (dist[agv.end] == Integer.MAX_VALUE) {
            System.out.println("起点和终点不连通");
            return null;
        } else {
            Stack<Integer> stack = new Stack<>();
            int p = agv.end;
            while (p != -1) {
                stack.push(p);
                p = prev[p];
            }
            ArrayList<Integer> ans = new ArrayList<Integer>();
            while (!stack.isEmpty())
                ans.add(stack.pop());
            // System.out.printf("\n从%d到%d的最短路径长度为：%d\n", agv.start, agv.end,
            // dist[agv.end]);
            return ans;
        }
    }

    private static int getDist(Graph g, int from, int to) {
        ArrayList<int[]> fromEdge = g.adjList[from];
        for (int[] a : fromEdge) {
            if (a[0] == to) {
                return a[1];
            }
        }
        return 0;
    }
}
