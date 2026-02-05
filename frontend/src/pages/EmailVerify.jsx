import React, { useEffect, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useRef, useState } from 'react';

import { assets } from '../assets/assets';
import { AppContext } from '../context/Appcontext';
import { toast } from 'react-toastify';
import axios from 'axios';


const EmailVerify = () => {

    const OTP_LENGTH = 6;
    const [otp, setOtp] = useState(Array(OTP_LENGTH).fill(""));
    const inputsRef = useRef([]);
    const { getUserData, isLoggedIn, userData , backendURL } = useContext(AppContext);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e, index) => {
        const value = e.target.value;

        // Allow only digits
        if (!/^\d?$/.test(value)) return;

        const newOtp = [...otp];
        newOtp[index] = value;
        setOtp(newOtp);

        if (value && index < OTP_LENGTH - 1) {
            inputsRef.current[index + 1].focus();
        }
    };

    const handleKeyDown = (e, index) => {
        if (e.key === "Backspace") {
            if (otp[index]) {
                // Clear current box
                const newOtp = [...otp];
                newOtp[index] = "";
                setOtp(newOtp);
            } else if (index > 0) {
                // Move backward
                inputsRef.current[index - 1].focus();
            }
        }
    };


    const handlePaste = (e) => {
        e.preventDefault();

        const pastedData = e.clipboardData
            .getData("text")
            .replace(/\D/g, "") // digits only
            .slice(0, OTP_LENGTH);

        if (!pastedData) return;

        const newOtp = [...otp];

        for (let i = 0; i < pastedData.length; i++) {
            newOtp[i] = pastedData[i];
        }

        setOtp(newOtp);

        const focusIndex =
            pastedData.length < OTP_LENGTH
                ? pastedData.length
                : OTP_LENGTH - 1;

        inputsRef.current[focusIndex]?.focus();
    };

    const handleVerifyOtp = async (e) => {
        e.preventDefault();
              const otpCode = inputsRef.current.map(input => input.value).join('');
              if(otpCode.length !== OTP_LENGTH) {
                toast.error("Please enter a valid 6-digit OTP");
                return;
              }
              setLoading(true);
              try { 
                    const response = await axios.post(backendURL+"/verify-otp", {otpCode}); 
                    if(response.status === 200) {
                        toast.success("Email verified successfully!");
                        getUserData();
                        navigate("/");
                    }else{
                        toast.error("Invalid OTP. Please try again.");
                    }
              } catch (error) {
                 toast.error("An error occurred while verifying OTP." + error.message);
              }finally{
                setLoading(false);
              }
    }

    useEffect(() => {
        if (isLoggedIn && userData && userData.isAccountVerified) {
            navigate("/");
        }
    }, [isLoggedIn, userData, navigate]);


    return (
        <div
            className="email-verify-container  min-h-screen bg-linear-to-br from-steel-gray-200 to-steel-gray-500 flex items-center justify-center relative"
        >
            <Link
                className="absolute top-0 start-0 p-4 flex items-center decoration-none"
                to="/">
                <img src={assets.logo} alt="logo"
                    className="h-16 w-16" />
                <span
                    className="text-4xl font-semibold text-dull-lavender-900"
                >AuthFlow</span>
            </Link>

            {/* Container for the Form  */}
            <div className="min-h-screen flex items-center justify-center  px-4">
                <div className="w-full max-w-md bg-white rounded-2xl shadow-xl p-6 sm:p-8">
                    <h2 className="text-2xl font-bold text-center text-gray-800">
                        Verify Your Email
                    </h2>
                    <p className="text-center text-gray-600 mt-2">
                        Enter the 6-digit OTP sent to your email
                    </p>

                    {/* Form for OTP verification */}
                    <form   className="mt-6">
                        {/* OTP Inputs */}
                        <div className="flex justify-between gap-2 sm:gap-3">
                            {otp.map((digit, index) => (
                                <input
                                    key={index}
                                    ref={(el) => (inputsRef.current[index] = el)}
                                    type="text"
                                    inputMode="numeric"
                                    maxLength={1}
                                    value={digit}
                                    onChange={(e) => handleChange(e, index)}
                                    onKeyDown={(e) => handleKeyDown(e, index)}
                                    onPaste={handlePaste}
                                    className="w-12 h-12 sm:w-14 sm:h-14 text-center text-xl font-semibold border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                />
                            ))}
                        </div>

                        {/* Submit Button */}
                        <button
                            type="submit"
                            className="w-full mt-6 bg-blue-600 text-white py-2.5 rounded-lg font-semibold hover:bg-blue-700 transition disabled:opacity-50 cursor-pointer"
                            disabled={otp.join("").length !== 6}
                            onClick={handleVerifyOtp}
                        >
                            {loading ? "Verifying..." : "Verify Email"}
                        </button>

                        {/* Resend */}
                        <p className="text-center text-sm text-gray-500 mt-4">
                            Didn’t receive the code?{" "}
                            <button
                                type="button"
                                className="text-blue-600 font-medium hover:underline cursor-pointer"
                            >
                                Resend OTP
                            </button>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default EmailVerify;