import { useRef, useState, useContext } from "react";
import { assets } from "../assets/assets";
import { X, Menu } from 'lucide-react';
import { Link, useNavigate } from "react-router-dom";
import { useEffect } from "react";

import { AppContext } from "../context/Appcontext";
import axios from "axios";
import { ArrowRight } from 'lucide-react';
import { toast } from "react-toastify";

const Menubar = () => {


    const navigate = useNavigate();
    const { userData, backendURL, setUserData, setIsLoggedIn } = useContext(AppContext);
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
                setDropdownOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);


    const handleLogout = async () => {
        // Implemented logout functionality here
        try {
            axios.defaults.withCredentials = true;
            const response = await axios.post(backendURL + '/logout');
            if (response.status === 200) {
                setIsLoggedIn(false);
                setUserData(null);
                toast.success("Logged out successfully");
                navigate("/");
            }
        } catch (error) {
            toast.error("Error during logout: " + error.message);
        }
    }

    const sendVerificationOTP = async () => {
        try {
            axios.defaults.withCredentials = true;
            const response = await axios.post(backendURL + "/send-otp");
            if (response.status === 200) {
                navigate("/email-verify");
                toast.success("Verification OTP sent to your email!");
            } else {
                toast.error("Failed to send verification OTP. Please try again.");
            }
        } catch (error) {
            toast.error("An error occurred while sending OTP." + error.response.data.message);
        }
    }

    return (
        <nav className="px-5  py-8 flex justify-between items-center">
            {/* left side = logo + text */}
            <Link className="flex items-center" to="/">
                <img src={assets.logo} alt="logo" className="h-16 w-16 object-contain cursor-pointer" />
                <span className="text-3xl font-semibold text-dull-lavender-900 cursor-pointer">Auth<span className="text-blue">Flow</span></span>
            </Link>

            {/* Right side = Action button */}
            <div className="flex items-center gap-4">
                {userData ? (
                    <div className="relative" ref={dropdownRef}>
                        <div
                            className="bg-gray-100 text-dull-lavender-600 font-semibold text-3xl rounded-full flex justify-center items-center cursor-pointer w-10 h-10"
                            onClick={() => { setDropdownOpen((prev) => !prev) }}
                        >
                            {userData.name.charAt(0).toUpperCase()}
                        </div>
                        {dropdownOpen && (
                            <div
                                className="absolute right-0 z-50 mt-2 w-40 bg-white rounded-lg  shadow-lg"
                            >
                                {!userData.isAccountVerified && (
                                    <button
                                        className="w-full text-left px-4 py-2 text-lg hover:bg-gray-200 cursor-pointer"
                                        onClick={sendVerificationOTP}
                                    >
                                        Verify Email
                                    </button>
                                )}
                                <button
                                    className="w-full text-left px-4 py-2 text-lg hover:bg-gray-200 cursor-pointer text-red-500"
                                    onClick={handleLogout}
                                    >
                                    Logout
                                </button>
                            </div>
                        )}
                    </div>
                ) : (
                    <div className=" flex items-center">
                        <button className="font-medium text-dull-lavender-900 border-2 rounded-full px-4 py-2 transition-all hover:text-dull-lavender-200 hover:bg-steel-gray-800 cursor-pointer flex"
                            onClick={() => navigate("/login")}>
                            Login<ArrowRight />
                        </button>
                    </div>
                )}
            </div>

        </nav>
    )
};

export default Menubar;