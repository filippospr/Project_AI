import random, sys, os

def shuffle(n):
	l = ["M" for i in range(n)] + ["A" for i in range(n)] + ["-"]
	random.shuffle(l)
	return "".join(l)

def main():
	if len(sys.argv) != 2:
		print('Give an number n as an argument')
		return
	else:
		n = int(sys.argv[1])
	s = shuffle(n)
	print(s)
	with open("string.txt", "w") as f:
		f.write(s)
	os.system("javac UCS.java")
	os.system("java UCS < string.txt")
	os.remove("string.txt")

if __name__ == '__main__':
	main()