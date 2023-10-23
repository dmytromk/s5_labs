package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Graph struct {
	tb [][]int
	mx sync.RWMutex
}

func main() {
	graph := &Graph{}

	rand.Seed(time.Now().UnixNano())

	go changeTicketPriceThread(graph)
	go addRemovePathsThread(graph)
	go addRemoveCitiesThread(graph)
	go findPathThread(graph)

	select {}
}

func getTwoRandomCities(size int) (int, int) {
	cityA := rand.Intn(size)
	cityB := rand.Intn(size - 1)
	if cityA == cityB {
		cityB = size - 1
	}
	return cityA, cityB
}

func getRandomPrice() int {
	return rand.Intn(10)
}

func printMatrix(matrix [][]int) {
	for i := 0; i < len(matrix); i++ {
		for j := 0; j < len(matrix[i]); j++ {
			fmt.Printf("%v ", matrix[i][j])
		}
		fmt.Print("\n")
	}
}

func changeTicketPriceThread(graph *Graph) {
	for {
		graph.mx.Lock()
		size := len(graph.tb)

		if size > 1 {
			cityA, cityB := getTwoRandomCities(size)
			newPrice := getRandomPrice()
			graph.tb[cityA][cityB] = newPrice
			graph.tb[cityB][cityA] = newPrice

			fmt.Printf("Changed ticket price from city %d to city %d: $%d\n", cityA, cityB, newPrice)

			printMatrix(graph.tb)
		}

		graph.mx.Unlock()

		time.Sleep(time.Second * 3)
	}
}

func addRemovePathsThread(graph *Graph) {
	for {
		graph.mx.Lock()
		size := len(graph.tb)

		if size > 1 {
			cityA, cityB := getTwoRandomCities(len(graph.tb))

			if graph.tb[cityA][cityB] != 0 {
				price := getRandomPrice()
				graph.tb[cityA][cityB] = price
				graph.tb[cityB][cityA] = price
				fmt.Printf("Added flight from city %d to city %d: $%d\n", cityA, cityB, price)
			} else {
				graph.tb[cityA][cityB] = 0
				graph.tb[cityB][cityA] = 0
				fmt.Printf("Removed flight from city %d to city %d\n", cityA, cityB)
			}

			printMatrix(graph.tb)
		}

		graph.mx.Unlock()

		time.Sleep(time.Second * 3)
	}
}

func addRemoveCitiesThread(graph *Graph) {
	for {
		graph.mx.Lock()
		size := len(graph.tb)

		choice := rand.Intn(2)

		if choice == 0 {
			newCityIndex := len(graph.tb)
			for i := range graph.tb {
				graph.tb[i] = append(graph.tb[i], 0)
			}
			newRow := make([]int, newCityIndex+1)
			graph.tb = append(graph.tb, newRow)

			fmt.Printf("Added a new city\n")
			printMatrix(graph.tb)
		} else {
			if size > 1 {
				cityToRemove := rand.Intn(size)
				graph.tb = removeCity(graph.tb, cityToRemove)

				fmt.Printf("Removed city %d\n", cityToRemove)
				printMatrix(graph.tb)
			}
		}

		graph.mx.Unlock()

		time.Sleep(time.Second * 3)
	}
}

func findPathThread(graph *Graph) {
	for {
		graph.mx.RLock()
		size := len(graph.tb)

		if size > 1 {
			cityA, cityB := getTwoRandomCities(len(graph.tb))
			price, path := findPath(graph.tb, cityA, cityB)
			if price == 0 {
				fmt.Printf("No path found from city %d to city %d\n", cityA, cityB)
			} else {
				fmt.Printf("Path found from city %d to city %d: Price $%d, Path: %v\n", cityA, cityB, price, path)
			}

			printMatrix(graph.tb)
		}

		graph.mx.RUnlock()

		time.Sleep(time.Second * 3)
	}
}

func removeCity(graph [][]int, cityToRemove int) [][]int {
	for i := range graph {
		graph[i] = append(graph[i][:cityToRemove], graph[i][cityToRemove+1:]...)
	}
	graph = append(graph[:cityToRemove], graph[cityToRemove+1:]...)
	return graph
}

func findPath(graph [][]int, start, end int) (int, []int) {
	const inf = 1e9

	n := len(graph)
	dist := make([]int, n)
	for i := range dist {
		dist[i] = inf
	}
	dist[start] = 0

	parent := make([]int, n)
	for i := range parent {
		parent[i] = -1
	}

	visited := make([]bool, n)

	for i := 0; i < n; i++ {
		v := -1
		for j := 0; j < n; j++ {
			if !visited[j] && (v == -1 || dist[j] < dist[v]) {
				v = j
			}
		}

		if dist[v] == inf {
			break
		}

		visited[v] = true

		for u := 0; u < n; u++ {
			if graph[v][u] > 0 && dist[v]+graph[v][u] < dist[u] {
				dist[u] = dist[v] + graph[v][u]
				parent[u] = v
			}
		}
	}

	if dist[end] == inf {
		return 0, nil
	}

	path := []int{end}
	for v := end; v != start; v = parent[v] {
		path = append([]int{parent[v]}, path...)
	}

	return dist[end], path
}
