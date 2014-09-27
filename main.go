package main

import (
	"fmt"
	"bytes"
	"regexp"
)

type token struct {
	name String
	value String
}


func tokenizer(in chan rune, out chan token) {
	stack := new(bytes.Buffer);
	continue := true;

	for continue == true {
		select {
			case nextchar, open := <-in:
				if(!open) {
					continue = false;
				}
				else {
					stack.WriteRune(nextchar)
					switch thing := stack.Bytes() {
						case regexp.Match("", thing):
							out<-
					}
				}
		}
	}
}

func main() {
    fmt.Println("Hello decaf")
}
