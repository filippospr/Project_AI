import random, sys, os

def shuffle(n):
	l = ["M" for i in range(n)] + ["A" for i in range(n)] + ["-"]
	random.shuffle(l)
	return "".join(l)

def main(n):
	s = shuffle(n)
	print('The input will be:', s)
	#write the string in a file
	with open("string.txt", "w") as f:
		f.write(s)
	os.system("javac A_star.java") #compile UCS.java
	os.system("java A_star < string.txt") #give the file as input to UCS
	os.remove("string.txt") #remove file

if __name__ == '__main__':
	if len(sys.argv) != 2:
		print('Give an number n as an argument')
	else:
		n = int(sys.argv[1])
		main(n)