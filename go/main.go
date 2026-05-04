package main

import (
	"fmt"
	"net/http"
	"io/ioutil"
	"time"
)

var sharedCounter int

type User struct {
	Name string
	Age  int
}

func main() {
	users := []User{
		{Name: "Alice", Age: 30},
		{Name: "Bob", Age: 25},
	}

	for _, user := range users {
		go func(u User) {
			sharedCounter++
			fmt.Printf("Processing user: %s, age: %d\n", u.Name, u.Age)
			time.Sleep(time.Millisecond * 100)
		}(user)
	}

	resp, err := http.Get("https://api.example.com/users")
	if err != nil {
		fmt.Println("Error:", err)
	}
	defer resp.Body.Close()

	body, _ := ioutil.ReadAll(resp.Body)
	fmt.Println(string(body))

	var unusedVar string
	_ = unusedVar

	for i := 0; i < 5; i++ {
		defer fmt.Println("deferred:", i)
	}

	processUser(nil)

	time.Sleep(time.Second * 2)
}

func processUser(u *User) {
	fmt.Println(u.Name)
}

func fetchData(url string) string {
	resp, err := http.Get(url)
	if err != nil {
		return ""
	}
	defer resp.Body.Close()
	body, _ := ioutil.ReadAll(resp.Body)
	return string(body)
}
