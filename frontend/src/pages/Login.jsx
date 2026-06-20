import { useContext, useState } from "react";
import { Link } from "react-router-dom";
import { assets } from "../assets/assets";
import { Eye, EyeOff } from "lucide-react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { AppContext } from "../context/Appcontext";

const Login = () => {

    const [showPassword, setShowPassword] = useState(false);
    const [isCreateAccount, setIsCreateAccount] = useState(false);

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const { backendURL, setIsLoggedIn, getUserData } = useContext(AppContext);

    const onSubmitHandler = async (e) => {
        e.preventDefault();
        axios.defaults.withCredentials = true;
        console.log("backend : " + backendURL);
        console.log(import.meta.env.VITE_API_URL);
        setLoading(true);
        try {
            if (isCreateAccount) {
                // Register User
                const response = await axios.post(`${backendURL}/register`, {
                    name, email, password
                });
                if (response.status === 201) {
                    navigate("/login");
                    toast.success("Registration Successful. Please Login.");
                } else {
                    toast.error("Registration Failed. Email already exist.");
                }
            } else {
                // Login User
                const response = await axios.post(`${backendURL}/login`, {
                    email, password
                });
                if (response.status === 200) {
                    localStorage.setItem(
                        "token",
                        response.data.token
                    );
                    setIsLoggedIn(true);
                    toast.success("Login Successful");
                    getUserData();
                    navigate("/");
                } else {
                    toast.error("Login Failed. Invalid Credentials.");
                }
            }
        } catch (error) {
            toast.error(error?.response?.data?.message || error.message || "Something went wrong!");
            setLoading(false);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="relative flex items-center justify-center min-h-screen bg-linear-to-br from-steel-gray-200 to-steel-gray-500">
            <div className="absolute top-5 left-8 flex items-center py-3">
                <Link
                    to="/"
                    className="flex items-center text-3xl font-bold no-underline"
                >
                    <img src={assets.logo} alt="logo" className="h-16 w-16" />
                    <span className=" text-dull-lavender-900">AuthFlow</span>
                </Link>
            </div>
            {/* Login Card */}
            <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-6 sm:p-8 mx-4">

                {
                    isCreateAccount ?
                        <h2 className="text-xl sm:text-2xl font-bold text-center text-gray-800">
                            Create Your Account
                        </h2> :
                        <h2 className="text-xl sm:text-2xl font-bold text-center text-gray-800">
                            Welcome Back
                        </h2>

                }

                <p className="text-center text-gray-500 mt-2">
                    {isCreateAccount
                        ? "Please enter your details to create an account."
                        : "Please enter your details to login."}
                </p>

                {/* Form */}
                <form onSubmit={onSubmitHandler} className="mt-6 space-y-4">
                    {/* Full Name Input */}
                    {isCreateAccount && (
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Full Name
                            </label>
                            <input
                                type="text"
                                placeholder="John Doe"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                required
                                onChange={(e) => { setName(e.target.value) }} />
                        </div>
                    )}

                    {/* Email Input */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Email
                        </label>
                        <input
                            type="email"
                            placeholder="you@example.com"
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                            required
                            onChange={(e) => { setEmail(e.target.value) }} />
                    </div>

                    {/* Password Input */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Password
                        </label>

                        <div className="relative">
                            <input
                                type={showPassword ? "text" : "password"}
                                placeholder="••••••••"
                                className="w-full px-4 py-2 pr-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                required
                                onChange={(e) => { setPassword(e.target.value) }} />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute inset-y-0 right-3 flex items-center text-gray-500"
                            >
                                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                            </button>
                        </div>
                    </div>

                    {/* Forgot Password Link */}
                    {!isCreateAccount && (
                        <p className="text-center text-sm text-gray-500 mt-2">
                            Forgot your password?{" "}
                            <Link to="/reset-password" className="text-blue-600 font-medium hover:underline">
                                Reset Password
                            </Link>
                        </p>
                    )}

                    {/* Submit Button */}
                    <button
                        type="submit"
                        className="w-full bg-dull-lavender-500 text-white py-2 rounded-lg font-semibold hover:bg-dull-lavender-700 transition"
                        disabled={loading}
                    >
                        {loading ? "loading..." : (isCreateAccount ? "Register" : "Login")}
                    </button>

                    {/* Create Account Link */}
                    {isCreateAccount ?

                        <p className="text-center text-sm text-gray-500 mt-2">
                            Already have an account?{" "}
                            <span className="text-blue-600 font-medium hover:underline cursor-pointer"
                                onClick={() => { setIsCreateAccount(false) }}>
                                Login Here
                            </span>
                        </p> : <p className="text-center text-sm text-gray-500 mt-2 cursor-pointer">
                            Don't have Account?{" "}
                            <span className="text-blue-600 font-medium hover:underline"
                                onClick={() => { setIsCreateAccount(true) }}>
                                Create Account
                            </span>
                        </p>
                    }
                </form>


            </div>
        </div>
    );
};


export default Login;
