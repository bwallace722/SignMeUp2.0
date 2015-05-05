import random, string, sys

courses = ["0170", "0320", "0330", "0510"]
category_names = ["homeworks", "labs", "exams", "projects"]
students = []
categories = []
assignments = []
topics = []
questions = []

num_students = 100
num_assignments = 100
num_topics = 1000
num_questions = 3000
num_visits = 10000

category_file_url = "category.csv"
student_file_url = "student.csv"
assignment_file_url = "assignment.csv"
topic_file_url = "topic.csv"
question_file_url = "question.csv"
hours_file_url = "hours.csv"
student_course_file_url = "student_course.csv"


def random_word(size=10, chars=string.ascii_uppercase + string.digits + string.ascii_lowercase):
	return ''.join(random.choice(chars) for _ in range(size))

#make up a bunch of students:

f = open(student_file_url, 'w')
print("id,name,login", file=f)
for i in range(num_students):
	s_id = i
	s_name = random_word()
	s_login = random_word(3, string.ascii_lowercase)
	students.append((s_id, s_name, s_login))
	print(s_id, ",", s_name, ",", s_login, sep='', file=f)

#add each of the category_names for each of the courses:
#id,name,course_id
f = open(category_file_url, 'w')
print("id,name,course_id", file=f)
category_id = 0
for course in courses:
	for category in category_names:
		categories.append((category_id, category, course))
		print(category_id, ",", category, ",", course, sep='', file=f)
		category_id += 1

#make up assignments
#id,name,category_id
f = open(assignment_file_url, 'w')
print("id,name,category_id", file=f)
for i in range(num_assignments):
	name = random_word() + str(i)
	category_id = random.choice(categories)[0]
	assignments.append((i, name, category_id))
	print(i, ",", name, ",", category_id, sep='', file=f)



#make up topics
#id, name, assignment_id
f = open(topic_file_url, 'w')
print("id,name,assignment_id", file=f)
for i in range(num_topics):
	name = random_word() + str(i)
	assignment_id = random.choice(assignments)[0]
	topics.append((i, name, assignment_id))
	print(i, ",", name, ",", assignment_id, sep='', file=f)


#make up questions
#id, text, topic_id
f = open(question_file_url, 'w')
print("id,name,topic_id", file=f)
for i in range(num_questions):
	name = "What is " + random_word(5) + " " + random_word(10) + "?"
	topic_id = random.choice(topics)[0]
	questions.append((i, name, topic_id))
	print(i, ",", name, ",", topic_id, sep='', file=f)


#have students randomly visit hours
#id, student_id, sign_up_time, check_off_time, question

f = open(hours_file_url, 'w')
print("id,student_id,sign_up_time,check_off_time,question", file=f)
for i in range(num_visits):
	student_id = random.choice(students)[0]
	sign_up_time = str(random.randint(0, sys.maxsize))
	check_off_time = str(random.randint(0, sys.maxsize))
	question = random.choice(questions)[0]
	print(i, ",", student_id, ",", sign_up_time, ",", check_off_time, ",", question, ",", sep='', file=f)


#every student is in every class for now
#id,student_id,class_id
i = 0
f = open(student_course_file_url, 'w')
print("id,student_id,class_id", file=f)
for s in students:
	for course in courses:
		print(str(i), ",", s[0], ",", course, sep='', file=f)
		i += 1
