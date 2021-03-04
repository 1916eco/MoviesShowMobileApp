package uk.ac.rgu.showlist.database;


import android.content.Context;

import java.util.List;

import uk.ac.rgu.showlist.Show;

public class SeenRepository {
    private static SeenRepository INSTANCE;

    private Context context;

    private ShowDao showDao;

    /**
     *The Repository is the connection between the applicatiojn and the data access object
     *
     *
     */

    public static SeenRepository getRepository(Context context){
            if (INSTANCE == null){
                synchronized (SeenRepository.class){
                    if (INSTANCE == null){
                        INSTANCE = new SeenRepository();
                        INSTANCE.context = context;
                        //setup DAO
                        INSTANCE.showDao = SeenDatabase.getDatabase(context).showDao();
                    }
                }
            }

            return INSTANCE;
        }

    public void storeSeenShows(Show show){
        this.showDao.insert(show);
    }

    public List<Show> getSeenShows(String listNamePassed){
        return showDao.getAllWatchedShows(listNamePassed);
    };

    public List<Show> getSearchedShows(String showTitle, String listNamePassed){
        return showDao.findShowByName(showTitle,listNamePassed);
    }

    public void deleteAllShows(String listNamePassed){
        showDao.deleteAllbyList(listNamePassed);
    }

    public void deleteShowbyName(String showTitle,String listNamePassed){
        showDao.deleteByName(showTitle,listNamePassed);
    }

    public void deleteAll(){
        showDao.deleteAll();
    }
    public int getCountAll(){
        return showDao.getCountAll();
    }
}
