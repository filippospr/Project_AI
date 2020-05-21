import os,sys
def main(s):
	#write the string in a file
	with open("string.txt", "w") as f:
		f.write(s)
	print('Running UCS on input',s)
	os.system("javac UCS.java") #compile UCS.java
	os.system("java UCS < string.txt") #give the file as input to UCS
	print()
	print('Running A_star on input',s)
	os.system("javac A_star.java") #compile UCS.java
	os.system("java A_star < string.txt") #give the file as input to UCS
	os.remove("string.txt") #remove file

if __name__ == '__main__':
	if len(sys.argv) == 2:
		main(sys.argv[1])