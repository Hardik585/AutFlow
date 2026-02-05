import Menubar from "../components/Menubar.component.jsx";
import Header from "../components/Header.component.jsx";

const Home = () => {
    return (
        <div className="bg-steel-gray-200 min-h-screen">
            <Menubar />
            {/* <div className="flex flex-col items-center justify-center min-h-100"> */}
                <Header />
            {/* </div> */}
        </div>
    )
}

export default Home;