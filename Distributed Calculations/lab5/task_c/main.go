package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type CyclicBarrier struct {
	capacity int
	current  int
	mutex    sync.Mutex
	cond     *sync.Cond
}

func NewCyclicBarrier(count int) *CyclicBarrier {
	b := &CyclicBarrier{
		capacity: count,
		current:  count,
	}
	b.cond = sync.NewCond(&b.mutex)
	return b
}

func (b *CyclicBarrier) Await() {
	b.mutex.Lock()
	b.current--
	if b.current > 0 {
		b.cond.Wait()
	} else {
		b.current = b.capacity
		b.cond.Broadcast()
	}
	b.mutex.Unlock()
}

var arrayChannels [3]chan int

func SumCheck() bool {
	sums := make([]int, 3)

	// Receive sums from worker goroutines
	for i := 0; i < 3; i++ {
		sums[i] = <-arrayChannels[i]
	}

	fmt.Println("Sum 1 =", sums[0], ", Sum 2 =", sums[1], ", Sum 3 =", sums[2])

	return (sums[0] == sums[1] && sums[1] == sums[2])
}

func ArrayWorker(id int, array []int, barrier *CyclicBarrier, wg *sync.WaitGroup) {
	for {
		fmt.Println("Thread", id, " is waiting at the barrier.")

		sum := 0
		for i := range array {
			sum += int(array[i])
		}

		arrayChannels[id-1] <- sum
		arrayChannels[id-1] <- sum
		arrayChannels[id-1] <- sum

		barrier.Await() // Wait at the barrier

		// Check for the sum condition and exit if met
		if SumCheck() {
			break
		}

		// Modify array elements
		index := rand.Intn(len(array))
		switch rand.Intn(2) {
		case 0:
			array[index] += 1
		case 1:
			array[index] -= 1
		}

		time.Sleep(1 * time.Second)
	}

	fmt.Println("Thread", id, " exited.")
	wg.Done()
}

func main() {
	rand.Seed(time.Now().UnixNano())

	main_array := make([]int, 12)
	var wg sync.WaitGroup

	for i := range main_array {
		main_array[i] = int(rand.Intn(4))
	}

	for i := range arrayChannels {
		arrayChannels[i] = make(chan int, 3)
	}

	sub_array1 := main_array[:4]
	sub_array2 := main_array[4:8]
	sub_array3 := main_array[8:]

	br := NewCyclicBarrier(3)
	wg.Add(3)
	go ArrayWorker(1, sub_array1, br, &wg)
	go ArrayWorker(2, sub_array2, br, &wg)
	go ArrayWorker(3, sub_array3, br, &wg)
	wg.Wait()
	fmt.Println("Program finished.")
}
