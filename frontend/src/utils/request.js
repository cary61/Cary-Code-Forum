import axios from "axios";
import { success, error } from './message.js';
import config from '../config.json';

export default {
    act: (action_type, uid, text_id, text_type) => {
        return axios.post(config.restServerSocket + '/action/act', {
            action_type, uid, text_id, text_type
        })  .then(resp => resp.data)
            .then(data => {
                if (data.status != 200) {
                    error(data.msg);
                    return false;
                } else {
                    return true;
                }
            });
    },
    like: (uid, text_id, text_type) => {
        return axios.post(config.restServerSocket + '/action/like', {
            uid, text_id, text_type
        })  .then(resp => resp.data)
            .then(data => {
                if (data.status != 200) {
                    error(data.msg);
                    return false;
                } else {
                    return true;
                }
            });
    },
    hate: (uid, text_id, text_type) => {
        return axios.post(config.restServerSocket + '/action/hate', {
            uid, text_id, text_type
        })  .then(resp => resp.data)
            .then(data => {
                if (data.status != 200) {
                    error(data.msg);
                    return false;
                } else {
                    return true;
                }
            });
    },
    deleteArticle: (uid, article_id) => {
        return axios.delete(config.restServerSocket + '/forum_update/article', {
            params: {
                uid, 
                'id': article_id
            }
        })  .then(resp => resp.data)
            .then(data => {
                if (data.status != 200) {
                    error(data.msg);
                    return false;
                } else {
                    return true;
                }
            });
    }
};