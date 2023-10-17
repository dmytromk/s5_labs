package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var (
	matchSem   = make(chan struct{}, 1)
	paperSem   = make(chan struct{}, 1)
	tobaccoSem = make(chan struct{}, 1)
	smokeSem   = make(chan struct{}, 1)
	wg         sync.WaitGroup
)

func agent() {
	rand.Seed(time.Now().UnixNano())
	for {
		ingredient := rand.Intn(3)

		switch {
		case ingredient == 0:
			tobaccoSem <- struct{}{}
			fmt.Println("Matches + Paper")
		case ingredient == 1:
			paperSem <- struct{}{}
			fmt.Println("Tobacco + Matches")
		case ingredient == 2:
			matchSem <- struct{}{}
			fmt.Println("Paper + Tobacco")
		}

		<-smokeSem
	}
}

func smoker(name string, sem chan struct{}, needed string) {
	for {
		<-sem

		fmt.Printf("%s is making and smoking a cigarette.\n\n", name)
		time.Sleep(time.Millisecond * 1000)

		smokeSem <- struct{}{}
	}
	wg.Done()
}

func main() {
	wg.Add(3)

	go agent()

	go smoker("Smoker[tobacco]", tobaccoSem, "matches and paper")
	go smoker("Smoker[matches]", matchSem, "paper and tobacco")
	go smoker("Smoker[paper]", paperSem, "matches and tobacco")

	wg.Wait()
}
