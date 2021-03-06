package com.yibao.biggirl.model.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.yibao.biggirl.model.favoriteweb.FavoriteWebBean;
import com.yibao.biggirl.model.music.MusicBean;
import com.yibao.biggirl.model.music.MusicInfo;

import com.yibao.biggirl.model.greendao.FavoriteWebBeanDao;
import com.yibao.biggirl.model.greendao.MusicBeanDao;
import com.yibao.biggirl.model.greendao.MusicInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig favoriteWebBeanDaoConfig;
    private final DaoConfig musicBeanDaoConfig;
    private final DaoConfig musicInfoDaoConfig;

    private final FavoriteWebBeanDao favoriteWebBeanDao;
    private final MusicBeanDao musicBeanDao;
    private final MusicInfoDao musicInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        favoriteWebBeanDaoConfig = daoConfigMap.get(FavoriteWebBeanDao.class).clone();
        favoriteWebBeanDaoConfig.initIdentityScope(type);

        musicBeanDaoConfig = daoConfigMap.get(MusicBeanDao.class).clone();
        musicBeanDaoConfig.initIdentityScope(type);

        musicInfoDaoConfig = daoConfigMap.get(MusicInfoDao.class).clone();
        musicInfoDaoConfig.initIdentityScope(type);

        favoriteWebBeanDao = new FavoriteWebBeanDao(favoriteWebBeanDaoConfig, this);
        musicBeanDao = new MusicBeanDao(musicBeanDaoConfig, this);
        musicInfoDao = new MusicInfoDao(musicInfoDaoConfig, this);

        registerDao(FavoriteWebBean.class, favoriteWebBeanDao);
        registerDao(MusicBean.class, musicBeanDao);
        registerDao(MusicInfo.class, musicInfoDao);
    }
    
    public void clear() {
        favoriteWebBeanDaoConfig.clearIdentityScope();
        musicBeanDaoConfig.clearIdentityScope();
        musicInfoDaoConfig.clearIdentityScope();
    }

    public FavoriteWebBeanDao getFavoriteWebBeanDao() {
        return favoriteWebBeanDao;
    }

    public MusicBeanDao getMusicBeanDao() {
        return musicBeanDao;
    }

    public MusicInfoDao getMusicInfoDao() {
        return musicInfoDao;
    }

}
