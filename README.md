# course_registration_system_simulator

Objective: A course registration system based on mileage bidding, not a first-come-first-served basis.    
Description: Unlike Seoul National University, many universities run their course registration systems based on mileage bidding. The idea can be briefly summarized as follows.   
1. This system provides a certain amount of mileage (e.g., 72) to individual students every semester.          
The student can place a bid for a course with a certain amount of mileage.      
The bid amount will be decided based on how much he wishes to take the course.         
2. At a certain point, course registrations will be confirmed based on all students’ bids. Students who bid higher mileages have the priority for the course registration. 
3. I implemented key functions of the course registration system as guided by the following.     


Function 1: Search Course Information
Objective: Implement the List<Course> search(Map<String,Object> searchConditions, String sortCriteria) method in the Server class.    
Description: The method returns the list of courses matching the search conditions (given in the Map ) following the sorting criteria (given as the String ).   
● Inputs:       
○ The first parameter Map<String, Object> searchConditions can describe three different search conditions as below.  

“dept”:  String : The department providing the course    
“ay” : Integer : The academic year for the course      
“name” : String : Name of the course       

■ If the searchConditions is null or empty, then it means there are no specific search criteria. In this case, you should return the list of entire courses sorted by the sorting criteria.      
■ For the “dept” and “ay” criteria, the values should be identical to meet the search conditions.    
■ The “name” criterion is specially handled. For instance, when the search string is “Computer Engineering”, you should consider two keywords “Computer” and “Engineering” split by the space.     
Then, you should find the courses that contain both keywords, i.e., “Computer” and “Engineering” in the course name.      
To generalize this, you first need to identify multiple keywords split by the space from the search string, then find the courses containing all keywords in their names.      
The search should be case-sensitive.      
■ For keyword matching, check if the search keyword is identical to one of the words in the course name.      
For instance, when the search string is “Comp”, the course named “Computer Engineering” should not be contained in the search result.     
■ If multiple search conditions co-exist, you should search for the courses that match all the specified conditions.      
■ Assume that the keys are one of the three strings, i.e., “dept”, “ay”, “name”.      

○ The second parameter String sortCriteria describes the sorting criteria of the returned List<Course> . It can have the following four values.

1. "id” : Sort by the course id in ascending order.     
2. “name”:  Sort by the course name in the dictionary order.     
If the names are equal, sort by the course id in ascending order.    
3. “dept” : Sort by the department name in the dictionary order.      
If the department names are equal, sort by the course id in ascending order.     
4. “ay” : Sort by the academic year in ascending order.    
If the academic years are equal, sort by the course id in ascending order.    
■ Assume that course id is unique for all the courses.       
■ If sortCriteria is null or empty String, sort by the course id.      
■ Assume that the sortCriteria string is one of {null, empty String, “id”, “name”, “dept”, “ay”}.      
● Outputs:        
○ List<Course> : The list of the class Course instances satisfying the given search condition with the elements sorted by the given criteria.    

Function 2: Placing a Bid          
Objective: Implement the following two methods in the Server class:       
1) int bid(int courseId, int mileage, String userId)         
2) Pair<Integer, List<Bidding>> retrieveBids(String userId)       
Description: The bid method allows a student to place a bid for a course while the retrieveBids method returns the information of all placed bids.     

Firstly, I implemented the following bid method.     
int bid(int courseid, int mileage, String userId)       
● Inputs:        
○ int courseId : Course id that the user wants to place a bid.      
○ int mileage : The amount of mileage the user wants to bid.         
○ String userId : User id to specify the user.        
● Outputs:       
○ int : Error code specifying the status of the method call.         

<ErrorCode Description>     
SUCCESS:  The bidding is successful.        
IO_ERROR : IOException is thrown during the execution of the method.         
OVER_MAX_MILEAGE:  The sum of previously bid mileages and the current bid mileage exceeds the maximum allowable mileage.       
Note that the maximum mileage is defined as a static variable MAX_MILEAGE in the Config class.       
OVER_MAX_COURSE_MILEAGE: The given mileage exceeds the maximum allowable mileage per course.       
Note that the maximum mileage per course is defined as a static variable MAX_MILEAGE_PER_COURSE in the Config class.   
NEGATIVE_MILEAGE : The mileage is a negative integer.       
NO_COURSE_ID:  The given course id does not exist in the system.      
USERID_NOT_FOUND : The given user id does not exist in the system.        
● The ErrorCode class (in the skeleton code) defines constant integer values matching various types of errors.     
If no error occurs, place the bid and return SUCCESS . Otherwise, do not place the bid and return the proper error code.      
● When multiple errors occur, return the error code with the lowest value.      
Note:       
1. Each user gets a maximum amount of mileage to be spent in total. The maximum mileage is defined in the Config class (as a static variable MAX_MILEAGE = 72 ).   
2. There is maximum allowable mileage for each bid, which is defined in the Config class (as a static variable MAX_MILEAGE_PER_COURSE = 18 ).      
3. The bid information should be stored in files (not in the memory only).       
The bid information should not be removed even if the server has been stopped and restarted.    
4. More specifically, the bid information of a user should be stored in a file (named “data/Users/(user id)/bid.txt”).     
It is required to save the bid information in the “bid.txt” file for each bid method call.      
5. Assume that a valid student has a directory named with his/her ID under the “Users” directory, and has the file “bid.txt” in it.       
If the corresponding directory for a user id does not exist, it means that no such student exists.    
In such a case, the USERID_NOT_FOUND error should be returned. You may want to consider defining a User class to manage the user information.         
6. If there is a valid user id directory with no “bid.txt” file, the bidding is ignored. This case, IO_ERROR error should be returned.    
7. If the user already has a bid for the same course, you should replace the existing bid with the new amount.    
8. If the user bids 0 mileage for the previous bid course, then the previous bid should be canceled.       
   The canceled bidding should be removed from both the bidding list in the memory and "bid.txt".
9. If the user bids 0 mileage for the new course, then ignore the bid.        

Secondly, I implemented the following retrieveBids method.    
Pair<Integer, List<Bidding>> retrieveBids(String userId)     
● Inputs:     
○ String userId : User id to specify the user.       
● Outputs:      
○ Pair<Integer, List<Bidding>> : The key is the error code (defined in ErrorCode class). The value is the list of previously placed bids. The list of the biddings does
not need to be sorted.     

Function 3: Confirming Bids       
Objective: Implement the following two methods in the Server class.     
1) boolean confirmBids()          
2) Pair<Integer, List<Course>> retrieveRegisteredCourse(String userId)         
Description: The confirmBids method determines who will be registered for which courses based on the bids placed so far.     
boolean confirmBids ()        
● Outputs:       
○ boolean :        
■ false , if IOException is thrown during the execution of the method.        
■ true , otherwise.         
The clearing logic is as follows.          
1. The basic rule is that students who bid larger mileage to a course will be registered for the course if the quota is limited.      
2. If multiple students bid the same mileage for a course and all of them cannot fit into the quota of the course, the student who placed a smaller number of bids in total has the
priority. If this second criterion cannot break the tie, a student with the preceding user id (in the dictionary order. e.g. 2012-12345 precedes 2012-12435) gets the priority.     
3. The confirmed registrations (i.e., who is registered for which courses) should be stored in files.        
Similar to the bids, the information of the confirmed registrations should not be removed even if the server has been stopped and restarted.    
4. All stored bids should be removed after the confirmation. Remove all bid information in every users' “bid.txt” files whether the registration is successful or not.    
5. It is assumed that confirmBids will not be called consecutively before the user information is reset;      

Secondly, I implemented the retrieveRegisteredCourse method.    
Pair<Integer, List<Course>> retrieveRegisteredCourse(String userId)    
● inputs:    
○ String userId : User id specifying the user.          
● outputs:              
○ Pair<Integer,List<Course>> : The key is the error code. The value is the list of the Course instances that are confirmed to be registered.       
The list does not need to be sorted.      
<ErrorCode Description>    
1. SUCCESS : Successfully returned the user’s registered courses.      
2. USERID_NOT_FOUND : The given user id does not exist in the system.       
3. IO_ERROR IOException : is thrown during the execution of the method.     
Note:        
● Assume that retrieveRegisteredCourse is called only after confirmBids method is invoked.     
