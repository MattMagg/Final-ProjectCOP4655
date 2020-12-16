package com.example.finalprojectcop4655.Fragments;

import java.util.List;


    public class SearchResponse {


        public List<Business> businesses = null;

        public Integer total;


        public  class Business {

            public String id;

            public String alias;

            public String name;

            public String image_url;

            public List<Category> categories = null;


            public String reviews;


            /*public Business(){

            }*/
        }


        public class Location {


            public String address1;

            public String address2;

            public String address3;

            public String city;

            public String zipCode;

            public String country;

            public String state;

            public List<String> displayAddress = null;

        }


        public class Category {


            public String alias;

            public String title;


        }
    }

