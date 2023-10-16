package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func main() {
	rand.Seed(time.Now().UnixNano())

	strengthRanks := []int{10, 8, 6, 12, 14, 16, 20, 18}

	var wg sync.WaitGroup

	finalWinner := playTournament(strengthRanks, &wg)

	wg.Wait()

	fmt.Printf("The final winner is Player %d with a strength rank of %d\n", finalWinner.index, finalWinner.strengthRank)
}

type Player struct {
	index        int
	strengthRank int
}

func playGame(player1, player2 Player, resultCh chan<- Player, wg *sync.WaitGroup) {
	defer wg.Done()

	winner := player1
	if player2.strengthRank > player1.strengthRank {
		winner = player2
	}

	resultCh <- winner
}

func playTournament(players []int, wg *sync.WaitGroup) Player {
	resultCh := make(chan Player, len(players)/2)

	for i := 0; i < len(players)/2; i++ {
		player1 := Player{index: i * 2, strengthRank: players[i*2]}
		player2 := Player{index: i*2 + 1, strengthRank: players[i*2+1]}

		wg.Add(1)

		go playGame(player1, player2, resultCh, wg)
	}

	var winners []Player

	for winner := range resultCh {
		winners = append(winners, winner)
		fmt.Printf("Game: Player %d vs. Player %d -> Winner: Player %d\n", winner.index-1, winner.index, winner.index)

		if len(winners) == 2 {
			wg.Add(1)

			go func() {
				defer wg.Done()
				playGame(winners[0], winners[1], resultCh, wg)
			}()

			winners = nil
		}

		if len(winners) == 1 {
			return winners[0]
		}
	}

	return playTournament(players, wg)
}
