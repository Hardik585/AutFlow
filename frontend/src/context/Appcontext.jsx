import { createContext, useEffect } from "react";
import { useState } from "react";
import { AppConstants } from "../utils/constants.js";

import axios from "axios";
import { toast } from "react-toastify";

export const AppContext = createContext();

export const AppContextProvider = (props) => {

    // axios.defaults.withCredentials = true;

    const backendURL = AppConstants.API_BASE_URL;
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userData, setUserData] = useState(false);

   

    const getUserData = async () => {
        try {
            const response = await axios.get(backendURL + "/profile");
            if (response.status === 200) {
                setUserData(response.data);
            } else {
                toast.error("Failed to fetch user data");
            }
        } catch (error) {
            toast.error("Something went wrong while fetching user data" + error.message);
        }

    }
    const contextValue = {
        backendURL,
        isLoggedIn, setIsLoggedIn,
        userData, setUserData, getUserData
    };

    useEffect(() => {           
        axios.defaults.withCredentials = true;
        const getAuthState = async () => {
            try {
                const response = await axios.get(backendURL + "/is-authenticated");
                if (response.status === 200 && response.data === true) {
                    setIsLoggedIn(true);
                    await getUserData();
                } else {
                    setIsLoggedIn(false);
                }
            } catch (err) {
                console.error("Error checking auth state:", err);
                setIsLoggedIn(false);
            }
        }
        (async () => {
            await getAuthState();
        })();
    }, []);


    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    )
}