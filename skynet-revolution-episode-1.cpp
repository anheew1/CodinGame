#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <queue>

using namespace std;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
struct Node {
  int number;
  int distance;
  int prev;
};



vector<int> AdjNodeNum(Node u, int links[][2], int l) {
  vector<int> adjList;
  for (int i = 0; i < l; i++) {
	int* link = links[i];
	if (u.number == link[0]) {
	  adjList.push_back(link[1]);
	}
	else if (u.number == link[1]) {
	  adjList.push_back(link[0]);
	}
  }
  return adjList;
}

Node* BFS(int si, int N, int links[][2], int l) {
  static Node nodes[500];
  for (int i = 0; i < N; i++) {
	nodes[i].number = i;
	nodes[i].distance = -1;
	nodes[i].prev = -1;
  }

  queue<Node> q;

  Node& s = nodes[si];
  s.distance = 0;
  q.push(s);
  while (!q.empty()) {
	Node u = q.front();
	q.pop();
	for (int vi : AdjNodeNum(u, links, l)) {
	  Node& v = nodes[vi];
	  if (v.distance == -1) {
		v.distance = u.distance + 1;
		v.prev = u.number;
		q.push(v);
	  }
	}
  }

  for (int i = 0; i < N; i++) {
	cerr << "Node " << nodes[i].number << " distance: " << nodes[i].distance << endl;
  }

  return nodes;


}


int main()
{
  int N; // the total number of nodes in the level, including the gateways
  int L; // the number of links
  int E; // the number of exit gateways
  int links[1000][2];
  int exits[20];

  cin >> N >> L >> E; cin.ignore();
  for (int i = 0; i < L; i++) {
	int N1; // N1 and N2 defines a link between these nodes
	int N2;
	cin >> N1 >> N2; cin.ignore();

	links[i][0] = N1;
	links[i][1] = N2;
  }
  for (int i = 0; i < E; i++) {
	int EI; // the index of a gateway node
	cin >> EI; cin.ignore();

	exits[i] = EI;
  }

  // game loop
  while (1) {
	int SI; // The index of the node on which the Skynet agent is positioned this turn
	cin >> SI; cin.ignore();

	// Write an action using cout. DON'T FORGET THE "<< endl"
	// To debug: cerr << "Debug messages..." << endl;

	// nodes[i] = node at i 
	Node* nodes = BFS(SI, N, links, L);

	// Finding shortest exit
	int s_exit = 0;
	int min = N; // number maximum is N
	for (int ei = 0; ei < E; ei++) {
	  int ex = exits[ei];
	  if (nodes[ex].distance < min) {
		min = nodes[ex].distance;
		s_exit = ex;
	  }
	}

	// close Edge
	cout << s_exit << " " << nodes[s_exit].prev << endl;

  }
}