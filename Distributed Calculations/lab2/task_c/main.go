package main

import (
	"fmt"
	"math/rand"
	"time"
)

type Player struct {
	index        int
	strengthRank int
}

func main() {
	rand.Seed(time.Now().UnixNano())

	strengthRanks := []int{10, 8, 6, 12, 14, 16, 20, 18, 5} // Example with 9 players

	finalWinner := playTournament(strengthRanks)

	fmt.Printf("The final winner is Player %d with a strength rank of %d\n", finalWinner.index, finalWinner.strengthRank)
}

func playGame(player1, player2 Player) Player {
	if player1.strengthRank > player2.strengthRank {
		return player1
	}
	return player2
}

func playTournament(players []int) Player {
	var playerList []Player
	for i, strength := range players {
		playerList = append(playerList, Player{index: i, strengthRank: strength})
	}

	for len(playerList) > 1 {
		var nextRound []Player
		for i := 0; i < len(playerList); i += 2 {
			if i+1 < len(playerList) {
				winner := playGame(playerList[i], playerList[i+1])
				nextRound = append(nextRound, winner)
				fmt.Printf("Game: Player %d vs. Player %d -> Winner: Player %d\n", playerList[i].index, playerList[i+1].index, winner.index)
			} else {
				// Handle bye for odd player count
				nextRound = append(nextRound, playerList[i])
				fmt.Printf("Game: Player %d has a bye in this round.\n", playerList[i].index)
			}
		}
		playerList = nextRound
	}

	return playerList[0]
}
