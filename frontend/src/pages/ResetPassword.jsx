import { useRef, useState, useContext } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { toast } from "react-toastify";
import { Link } from "react-router-dom";
import { assets } from "../assets/assets";
import { AppContext } from "../context/Appcontext";

const ResetPassword = () => {

  const [params] = useSearchParams();
  const token = params.get("token");
  const inputsRef = useRef([]);
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isEmailSent, setIsEmailSent] = useState(false);
  const [otp, setOtp] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isOtpSubmitted, setIsOtpSubmitted] = useState(false);
  const OTP_LENGTH = 6;
  const [otpArr, setOtpArr] = useState(Array(OTP_LENGTH).fill(""));

  const { getUserData, isLoggedIn, userData, backendURL } = useContext(AppContext);

  axios.defaults.withCredentials = true;

  //function to handle new password submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      toast.error("Passwords do not match");
      return;
    }
    setLoading(true);
    try {
      await axios.post(backendURL+"/reset-password", {
        email: email,
        newPassword: password,
        otp: otp
      });
      toast.success("Password reset successfully");
      navigate("/login");

    } catch (err) {
      toast.error(err.response?.data?.message || "Reset failed");
    }finally {
      setLoading(false);
    }
  };


  // Logic for the otp inputs and verification start here
  const handleChange = (e, index) => {
    const value = e.target.value;
    // Allow only digits
    if (!/^\d?$/.test(value)) return;
    const newOtp = [...otpArr];
    newOtp[index] = value;
    setOtpArr(newOtp);
    if (value && index < OTP_LENGTH - 1) {
      inputsRef.current[index + 1].focus();
    }
  };

  const handleKeyDown = (e, index) => {
    if (e.key === "Backspace") {
      if (otpArr[index]) {
        // Clear current box
        const newOtp = [...otpArr];
        newOtp[index] = "";
        setOtpArr(newOtp);
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
    const newOtp = [...otpArr];
    for (let i = 0; i < pastedData.length; i++) {
      newOtp[i] = pastedData[i];
    }
    setOtpArr(newOtp);
    const focusIndex =
      pastedData.length < OTP_LENGTH
        ? pastedData.length
        : OTP_LENGTH - 1;
    inputsRef.current[focusIndex]?.focus();
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    const enteredOtp = otpArr.join("");
    setLoading(true);
    setOtp(enteredOtp);
    setIsOtpSubmitted(true);
  }

  {/* Handle otp send to the Email */ }
  const handleOTPSend = async (e) => {
    e.preventDefault();
    if (!email) {
      toast.error("Please enter your email");
      return;
    }
    setLoading(true);
    try {
      const response = await axios.post(`${backendURL}/send-reset-otp?email=${email}`);
      if (response.status === 200) {
        setIsEmailSent(true);
        toast.success("Reset Password otp sent to your email");
      } else {
        toast.error("Failed to send Reset OTP. Please try again.");
      }
    } catch (err) {
      toast.error(err.response?.data?.message + "Failed to Reset send OTP email is not registered");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative flex items-center justify-center min-h-screen bg-linear-to-br from-steel-gray-200 to-steel-gray-500">
      {/* Navbar Component */}
      <div className="absolute top-5 left-8 flex items-center py-3">
        <Link
          to="/"
          className="flex items-center text-3xl font-bold no-underline"
        >
          <img src={assets.logo} alt="logo" className="h-16 w-16" />
          <span className=" text-dull-lavender-900">AuthFlow</span>
        </Link>
      </div>

      {/*  Card to Enter Email to send reset-otp  */}
      {!isEmailSent && (
        <div className="min-h-screen flex items-center justify-center">
          <form className="bg-white p-6 rounded-xl shadow w-full max-w-sm">
            <h2 className="text-2xl font-bold text-center">Forgot Password</h2>
            <input
              type="email"
              placeholder="Enter your email"
              className="w-full mt-4 px-4 py-2 border rounded-lg"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <button
              type="button"
              disabled={loading}
              className="w-full mt-4 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700"
              onClick={handleOTPSend}>
              {loading ? "Sending..." : "Send Reset Link"}
            </button>
          </form>
        </div>
      )}


      {/* UI for the Otp  Input*/}
      {!isOtpSubmitted && isEmailSent && (
        <div className="w-full max-w-md bg-white rounded-2xl shadow-xl p-6 sm:p-8">
          <h2 className="text-2xl font-bold text-center text-gray-800">
            Verify Your Email
          </h2>
          <p className="text-center text-gray-600 mt-2">
            Enter the 6-digit OTP sent to your email
          </p>
          {/* Form for OTP verification */}
          <form className="mt-6">
            {/* OTP Inputs */}
            <div className="flex justify-between gap-2 sm:gap-3">
              {otpArr.map((digit, index) => (
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
              type="button"
              className="w-full mt-6 bg-blue-600 text-white py-2.5 rounded-lg font-semibold hover:bg-blue-700 transition disabled:opacity-50 cursor-pointer"
              disabled={otpArr.join("").length !== 6}
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
      )
      }
      {/* UI for the Otp  End*/}

      {/* New Password Form  */}
      {isOtpSubmitted && isEmailSent && (
        <div className="min-h-screen flex items-center justify-center">
          <form onSubmit={handleSubmit} className="bg-white p-6 rounded-xl shadow w-full max-w-sm">
            <h2 className="text-2xl font-bold text-center">Reset Password</h2>

            <input
              type="password"
              placeholder="New password"
              className="w-full mt-4 px-4 py-2 border rounded-lg"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <input
              type="password"
              placeholder="Confirm password"
              className="w-full mt-3 px-4 py-2 border rounded-lg"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />

            <button
              type="submit"
              className="w-full mt-4 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700"
            >
              Reset Password
            </button>
          </form>
        </div>
      )};
      {/* UI form for the new password end  */}
    </div >
  );
};

export default ResetPassword;
