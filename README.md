# VotingApplication
UTeM E-Undi
Mobile Final Project

The project aims to develop a voting mobile application to supplement the existing voting platform, which is a website (iUTeM e-Undi). 
The mobile application will provide voters with an alternate, convenient and efficient way to cast their votes. Key features of the 
application will include a user-friendly interface, secure login, and statistical vote tracking. The project will leverage the latest
software (Android Studio) to ensure a smooth and seamless experience for users. The objective is to increase voter participation and 
provide a more accessible voting process. The end goal is to create a reliable and efficient voting system that meets the needs of 
voters and enhances the overall democratic process.

Main features of this application:
1. Login : student need to login by using their matric number and password to get access into the application 
         //REMINDER for testing,  use -> (matric no : B032010342 , password : abc123)
         
2. Vote : student allow to vote the candidates following the rules that will appear in the home page.

Additional features :
1. Change password : student able to change password by clicking up the forgot password button in login page.
2. View Candidate List : student can view all the candidates which participate for the election based on their faculty
3. View votes count (report) : student may view the latest count of votes after they successfully create their votes.

Database : Firebase FireStore

List of tables :
1. student : email, name, matric no. , password, faculty, year and semester
2. candidates : image, name, club, faculty
3. votes : image, name, club, faculty, count
// All datas in the tables will be provided a unique id which will be auto generated once the data is created.

Database function :
1. Table student = it will be used during login session, and changing password.
2. Table candidates = this application will retrieve the data from this table to display in Candidate List page. It will be differentiate 
                        by the field 'faculty' because of students only can view the candidates from their faculty.
3. Table vote = the data in this table will be keep updated after student making their votes for the candidates. In this table contain all
                the candidates include their personal information with additional field 'count' which will be maintain or increasing based 
                on student's votes. The data from this table also will be displayed in the Home page and allow student to keep track the
                current result of candidates.
                      
