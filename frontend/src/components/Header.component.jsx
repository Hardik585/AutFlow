import { assets } from "../assets/assets";
import { useContext } from "react";
import { AppContext } from "../context/Appcontext";


const Header = () => {

    const {userData} = useContext(AppContext);    

    return (
        <div className="text-center flex flex-col items-center justify-center px-3 ">
            <img src={assets.logo_home} alt="logo" style={{width:"300px"}}/>

            <h5 className="font-semibold text-2xl">
                Hey {userData ? userData.name : "Developer"} <span role="img" aria-label="wave">👋</span>
            </h5>

            <h1 className="font-bold display-5 mb-4 text-4xl">Welcome to our product </h1>
            <p className="text-muted font-size-5 mb-4" style={{ maxWidth: "500px" }}>
                Let's start by logging into your account to access all the features and services we offer.
            </p>
            <button className="font-medium text-dull-lavender-900 border-2 rounded-full px-4 py-2 transition-all  hover:text-dull-lavender-50 hover:bg-steel-gray-800 cursor-pointer">
                Get Started
            </button>

        </div>
    );

}
export default Header;