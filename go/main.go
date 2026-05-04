// Package main implements a demo for MegaLinter fixes.
package main

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"sync"
	"time"
)

var mu sync.Mutex

type User struct {
	Name string
	Age  int
}

func main() {
	users := []User{
		{Name: "Alice", Age: 30},
		{Name: "Bob", Age: 25},
	}

	var wg sync.WaitGroup
	for _, user := range users {
		wg.Add(1)
		go func(u User) {
			defer wg.Done()
			mu.Lock()
			sharedCounter++
			mu.Unlock()
			fmt.Printf("Processing user: %s, age: %d\n", u.Name, u.Age)
			time.Sleep(100 * time.Millisecond)
		}(user)
	}

	resp, err := http.Get("https://api.example.com/users")
	if err != nil {
		log.Printf("Error fetching users: %v", err)
	} else {
		defer resp.Body.Close()
		body, err := io.ReadAll(resp.Body)
		if err != nil {
			log.Printf("Error reading response: %v", err)
		} else {
			fmt.Println(string(body))
		}
	}

	processUser(User{Name: "Charlie", Age: 35})

	for i := 0; i < 5; i++ {
		fmt.Println("deferred:", i)
	}

	wg.Wait()
}

func processUser(u User) {
	fmt.Println(u.Name)
}

func fetchData(url string) string {
	resp, err := http.Get(url)
	if err != nil {
		log.Printf("Error fetching %s: %v", url, err)
		return ""
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Printf("Error reading response from %s: %v", url, err)
		return ""
	}
	return string(body)
}
