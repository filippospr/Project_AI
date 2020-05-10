import random, sys, os

def shuffle(n):
	n = int(sys.argv[1])
	l = ["M" for i in range(n)] + ["A" for i in range(n)] + ["-"]
	random.shuffle(l)
	return "".join(l)

if __name__ == '__main__':
	s = shuffle(sys.argv[1])
	print(s)
	with open("string.txt", "w") as f:
		f.write(s)
	os.system("javac UCS.java")
	os.system("java UCS < string.txt")
	os.remove("string.txt")