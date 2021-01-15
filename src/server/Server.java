package server;

import course.*;
import utils.ErrorCode;
import utils.Pair;

import java.io.*;

import java.util.*;

class User{
    public ArrayList<Course> registeredCourse=new ArrayList<>();
    public ArrayList<Bidding> biddedCourse=new ArrayList<>();
    public String UserID;
    public User(String userID){
        this.UserID=userID;
    }
}
public class Server {

    ArrayList<Course> courseArray = new ArrayList<>();

    public List<Course> sort(List<Course> newCourses, String sortCriteria) {

        if (sortCriteria == null||sortCriteria.equals("")||sortCriteria.length()==0)  {
            newCourses.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    if (o1.courseId < o2.courseId) {
                        return -1;
                    } else if (o1.courseId == o2.courseId) {
                        return 0;
                    } else {
                        return 1;
                    }

                }
            });
            return newCourses;
        } else if (sortCriteria.equals("id")) {
            newCourses.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    if (o1.courseId < o2.courseId) {
                        return -1;
                    } else if (o1.courseId == o2.courseId) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            return newCourses;
        } else if (sortCriteria.equals("name")) {
            newCourses.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    if (o1.courseName.equals(o2.courseName)) {
                        if (o1.courseId < o2.courseId) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        return o1.courseName.compareTo(o2.courseName);

                    }
                }
            });
            return newCourses;
        } else if (sortCriteria.equals("dept")) {
            newCourses.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    if (o1.department.equals(o2.department)) {
                        if (o1.courseId < o2.courseId) {
                        return -1;
                        } else if (o1.courseId == o2.courseId) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {


                        return o1.department.compareTo(o2.department);

                    }
                }
            });
            return newCourses;
        } else if (sortCriteria.equals("ay")) {
            newCourses.sort(new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    if (o1.academicYear == o2.academicYear) {
                        if (o1.courseId < o2.courseId) {
                            return -1;
                        } else if (o1.courseId == o2.courseId) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        if (o1.academicYear < o2.academicYear) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            });
            return newCourses;
        }
        newCourses.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                if (o1.courseId < o2.courseId) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });
        return newCourses;
    }


    public List<Course> search(Map<String, Object> searchConditions, String sortCriteria) {
        // TODO Problem 2-1
        courseArray=new ArrayList<>();
        File[] directory = new File("data/Courses/2020_Spring").listFiles();
        String courseID;
        String[] coursedetail;
        if (directory!=null){
        for (File adirectory : directory) {

            File[] realfile = adirectory.listFiles();
            String depa = adirectory.getName();
            for (File eachfile : realfile) {


                String content = "";
                courseID = eachfile.getName();

                try (FileInputStream fis = new FileInputStream(eachfile.getPath())) {
                    int i;
                    while ((i = fis.read()) != -1) {
                        content =content+ (char) i;
                    }
                    fis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                coursedetail = content.split("\\|");

                Course newCourse = new Course(Integer.parseInt(courseID.split("\\.")[0]), depa,
                        coursedetail[0], coursedetail[1], Integer.parseInt(coursedetail[2]), coursedetail[3],
                        Integer.parseInt(coursedetail[4]), coursedetail[5], coursedetail[6], Integer.parseInt(coursedetail[7]));
                if (!courseArray.contains(newCourse)) {
                    courseArray.add(newCourse);
                }
            }
        }}
        ArrayList<Course> selectedCourse = new ArrayList<>(courseArray);
        if (searchConditions == null || searchConditions.isEmpty()||searchConditions.size()==0) {
            return this.sort(courseArray, sortCriteria);

        }

        if (searchConditions.containsKey("dept")) {
            ArrayList<Course> tmplist=new ArrayList<Course>(selectedCourse);
            for (Course course : selectedCourse) {
                if (!course.department.equals((String)searchConditions.get("dept"))) {
                   tmplist.remove(course);
                }
            }
            selectedCourse=tmplist;
        }

        if (searchConditions.containsKey("ay")) {

                ArrayList<Course> tmplist=new ArrayList<Course>(selectedCourse);
                for (int k = 0; k < selectedCourse.size(); k++) {
                    Course course = selectedCourse.get(k);
                    if (course.academicYear != (Integer) searchConditions.get("ay")) {
                        tmplist.remove(course);
                    }
                }
                selectedCourse=tmplist;
            }


        if (searchConditions.containsKey("name")) {
            String namelist = ((String) searchConditions.get("name"));
            String[] namearray = namelist.trim().split(" ");
            ArrayList<Course> tmplist = new ArrayList<Course>(selectedCourse);

            for (int k = 0; k < selectedCourse.size(); k++) {

                Course course = selectedCourse.get(k);

                String[] names = course.courseName.trim().split(" ");


                for (int j = 0; j < namearray.length; j++) {
                    boolean samenameexist=false;
                  for (String name: names){
                      if (name.equals(namearray[j])){
                          samenameexist=true;
                      }
                  }
                  if (!samenameexist){
                      tmplist.remove(course);
                      break;
                  }
                }
            }
            selectedCourse = tmplist;



        }
       
        return this.sort(selectedCourse, sortCriteria);
    }

    public int bid(int courseId, int mileage, String userId) {
        // TODO Problem 2-2
        search(null, null);

        Pair<Integer, List<Bidding>> a = retrieveBids(userId);
        ArrayList<Bidding> alreadyBid = new ArrayList<>(a.value);
        if (a.key == -61) {
            return -61;
        }
        Course biddingCourse = null;
        for (Course course : courseArray) {
            if (course.courseId == courseId) {
                biddingCourse = course;
                break;
            }
        }
        if (biddingCourse == null) {
            return -51;
        }
        if (mileage < 0) {
            return -43;
        }
        if (mileage > 18) {
            return -42;
        }
        boolean alreadyExist = false;
        for (Bidding bid : alreadyBid) {
            if (bid.courseId == courseId) {
                if (mileage == 0) {
                    alreadyBid.remove(bid);
                    alreadyExist = true;
                    break;
                }
                alreadyBid.remove(bid);
                bid.mileage = mileage;
                alreadyBid.add(bid);
                alreadyExist = true;

                break;
            }
        }
        if (!alreadyExist) {
            if (mileage != 0) {
                alreadyBid.add(new Bidding(courseId, mileage));
            }
        }


        int sum = 0;
        for (Bidding bid : alreadyBid) {
            sum += bid.mileage;
        }

        if (sum > 72) {
            return -41;
        }


        if (a.key == -10) {
            return -10;
        }

        String content = "";
        FileInputStream fis = null;
        BufferedWriter writer = null;
        File file = null;

        try {
            fis = new FileInputStream("data/Users/" + userId + "/bid.txt");
            int i;
            while ((i = fis.read()) != -1) {
                content += (char) i;
            }
            String somethingWrite = "";
            for (Bidding bid : alreadyBid) {
                somethingWrite += bid.courseId + "|" + bid.mileage + "\n";
            }
            file = new File("data/Users/" + userId + "/bid.txt");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(somethingWrite);
        } catch (FileNotFoundException e) {


        } catch (IOException e) {

        } finally {
            try {
                fis.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public Pair<Integer, List<Bidding>> retrieveBids(String userId) {
        String content = "";
        // TODO Problem 2-2
        FileInputStream fis = null;
        try {
            ArrayList<Bidding> biddinglist = new ArrayList<>();
            if (userId.contains("./")){
                return new Pair(-61, new ArrayList<Bidding>());
            }
            fis = new FileInputStream("data/Users/" + userId + "/bid.txt");
            int i;
            while ((i = fis.read()) != -1) {
                content += (char) i;
            }
            String[] list_prep = null;
            if (content != null && content != "") {
                list_prep = content.split("\n");
            }
            if (list_prep != null && list_prep.length != 0) {
                for (String bid : list_prep) {
                    if (bid != null) {
                        if (bid.split("\\|").length >= 2) {
                            Bidding newBid = new Bidding(Integer.parseInt(bid.split("\\|")[0].trim()),
                                    Integer.parseInt(bid.split("\\|")[1].trim()));

                            biddinglist.add(newBid);
                        }
                    }
                }
            }
            return new Pair(0, biddinglist);
        } catch (FileNotFoundException e) {

            return new Pair(-61, new ArrayList<Bidding>());
        } catch (IOException e) {
            return new Pair(-10, new ArrayList<Bidding>());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {

            }


        }

    }


    public boolean confirmBids() {
        // TODO Problem 2-3
        ArrayList<User> users = new ArrayList<>();
        search(null, null);
        File[] Directory = new File("data/Users/").listFiles();
        ArrayList<String> UserID = new ArrayList<>();
        for (File a : Directory) {
            UserID.add(a.getName());
            User user = new User(a.getName());
            if (!users.contains(user)) {
                users.add(user);
            }

        }
        ArrayList<Pair> coursePut = new ArrayList<>();
        for (Course course : courseArray) {
            Pair prep = new Pair(course, new ArrayList<>());
            coursePut.add(prep);
        }
        for (User user : users) {
            ArrayList<Bidding> courseList = new ArrayList<>();
            if (retrieveBids(user.UserID).key == 0) {
                courseList = (ArrayList<Bidding>) retrieveBids(user.UserID).value;
                user.biddedCourse = courseList;
            }
            if (retrieveBids(user.UserID).key == -10) {
                FileWriter writer = null;
                try {
                    for (User auser : users) {
                        writer = new FileWriter("data/Users/" + auser.UserID + "/bid.txt", false);

                        writer.write("");
                        writer.close();
                    }
                } catch (FileNotFoundException e) {
                    try {
                        writer.close();
                    } catch (IOException en) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    try {
                        writer.close();
                    } catch (IOException en) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
            for (Bidding bid : courseList) {
                for (Pair pair : coursePut) {
                    if (bid.courseId == ((Course) pair.key).courseId) {
                        ArrayList<Bidding> tmparray = (ArrayList<Bidding>) pair.value;
                        tmparray.add(bid);
                        pair.value = tmparray;
                        break;
                    }
                }
            }
        }

        for (Pair pair : coursePut) {
            Course currentcourse = (Course) pair.key;
            ArrayList<Bidding> bidded = (ArrayList<Bidding>) pair.value;
            if (currentcourse.quota >= bidded.size()) {
                for (User auser : users) {
                    for (Bidding alreadyBidded : auser.biddedCourse) {
                        if (alreadyBidded.courseId == currentcourse.courseId) {
                            auser.registeredCourse.add(currentcourse);
                            break;
                        }
                    }
                }
            } else {
                bidded.sort(new Comparator<Bidding>() {
                    @Override
                    public int compare(Bidding o1, Bidding o2) {
                        if (o1.mileage < o2.mileage) {
                            return 1;
                        } else if (o1.mileage == o2.mileage) {
                            User u1 = null;
                            User u2 = null;
                            for (User user : users) {
                                for (Bidding bid : user.biddedCourse) {
                                    if (bid.courseId == o1.courseId && bid.mileage == o1.mileage) {
                                        u1 = user;
                                        break;
                                    }
                                }
                            }
                            for (User user : users) {
                                for (Bidding bid : user.biddedCourse) {
                                    if (bid.courseId == o2.courseId && bid.mileage == o2.mileage) {
                                        u2 = user;
                                        break;
                                    }
                                }
                            }
                            int sum1 = 0;
                            int sum2 = 0;
                            for (Bidding bidded : u1.biddedCourse) {
                                sum1 += bidded.mileage;
                            }
                            for (Bidding bidded : u2.biddedCourse) {
                                sum2 += bidded.mileage;
                            }
                            if (sum1 < sum2) {
                                return -1;
                            } else if (sum1 > sum2) {
                                return 1;
                            } else {
                                return u1.UserID.compareTo(u2.UserID);
                            }
                        } else {
                            return -1;
                        }
                    }
                });
                ArrayList<Bidding> confirmedList = new ArrayList<>();
                users.sort(new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        int sum1 = 0;
                        int sum2 = 0;
                        for (Bidding bid : o1.biddedCourse) {
                            sum1 += bid.mileage;
                        }
                        for (Bidding bid : o2.biddedCourse) {
                            sum2 += bid.mileage;
                        }
                        if (sum1 < sum2) {
                            return -1;
                        } else if (sum1 > sum2) {
                            return 1;
                        } else {
                            return o1.UserID.compareTo(o2.UserID);
                        }
                    }
                });
                for (int i = 0; i < currentcourse.quota; i++) {
                    confirmedList.add(bidded.get(i));
                }
                int count = 0;
boolean Ifoundit=false;
                for (Bidding bidding : confirmedList) {
                    Ifoundit=false;
                    for (User auser : users) {
                        for (Bidding bid : auser.biddedCourse) {
                            if ((bid.courseId == bidding.courseId) && (bid.mileage == bidding.mileage)) {
                                if (auser.registeredCourse.contains(currentcourse)){
                                    break;
                                }
                                auser.registeredCourse.add(currentcourse);
                                count++;
                                Ifoundit=true;
                                break;
                            }
                        }

                        if(Ifoundit){
                            break;
                        }
                    }

                }
            }

        }
        for (User user : users) {
            String tobeWrited = "";
            FileWriter record=null;
            try {
                 record= new FileWriter("data/Users/" + user.UserID + "/write.txt");
                for (Course course : user.registeredCourse) {
                    tobeWrited += course.courseId + ":" + course.college + ":" + course.department + ":" + course.academicDegree
                            + ":" + course.academicYear + ":" + course.courseName + ":" + course.credit + ":" + course.location +
                            ":" + course.instructor + ":" + course.quota + "\n";
                }
                record.write(tobeWrited);
                record.close();
            } catch (IOException e) {

              try{
                  record.close();
              } catch (IOException ioException) {
                  ioException.printStackTrace();
              }
            }
        }
        FileWriter writers = null;
        try {
            for (User auser : users) {
                File file = new File("data/Users/" + auser.UserID + "/bid.txt");
                writers = new FileWriter(file, false);

                writers.write("");
                writers.close();
            }
        } catch (FileNotFoundException e) {
            try {
                writers.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            try {
                writers.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return false;
        }

        return true;
    }

    public Pair<Integer, List<Course>> retrieveRegisteredCourse(String userId) {
        // TODO Problem 2-3


        boolean userFound = false;
        ArrayList<User> users = new ArrayList<>();
        search(null, null);
        File[] Directory = new File("data/Users/").listFiles();
        ArrayList<String> UserID = new ArrayList<>();
        for (File a : Directory) {
            UserID.add(a.getName());
            User user = new User(a.getName());
            if (!users.contains(user)) {
                users.add(user);
            }

        }
        User thisUser = null;
        for (User user : users) {
            if (user.UserID.equals(userId)) {
                userFound = true;
                thisUser = user;
                break;
            }
        }
        if (userFound == false) {
            return new Pair<>(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());
        }
        ArrayList<Course> courselist = new ArrayList<>();

        FileInputStream fis = null;
        try {

            fis = new FileInputStream("data/Users/" + thisUser.UserID + "/write.txt");
            String content = "";
            int i;
            while ((i = fis.read()) != -1) {
                content += (char) i;
            }
            String[] list_prep=null;
            String[] sublist=null;
            if (content!=null){
                list_prep= content.split("\n");}
            Course course = null;
            if (content==null || content.length()==0){
                return new Pair(ErrorCode.SUCCESS, new ArrayList<Course>());
            }
            if (content != null && content != "") {
                for(String str:list_prep) {
                    sublist = str.split(":");

                    if (sublist != null && sublist.length != 0) {
                        course = new Course(Integer.parseInt(sublist[0]), sublist[1], sublist[2], sublist[3],
                                Integer.parseInt(sublist[4]), sublist[5],
                                Integer.parseInt(sublist[6]), sublist[7], sublist[8], Integer.parseInt(sublist[9]));
                    }
                    courselist.add(course);
                }}
        } catch (FileNotFoundException e) {
            return new Pair(ErrorCode.IO_ERROR, courselist);
        } catch (IOException ioException) {
            return new Pair(ErrorCode.IO_ERROR, courselist);
        }


    return new Pair(ErrorCode.SUCCESS, courselist);
}}
