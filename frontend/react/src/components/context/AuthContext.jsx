import {
    createContext,
    useContext,
    useEffect,
    useState
} from "react"
import {login as performLogin} from "../../services/client.js";

const AuthContext = createContext({});

const AuthProvider = ({ children }) => {

    const [customer, setCustomer] = useState(null);
    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"];
                //save the token
                localStorage.setItem("access_token", jwtToken)
                console.log(jwtToken)
                setCustomer({
                    ...res.data.customerDTO
                })
                resolve(res);
            }).catch(err => {
                reject(err);
            })
        })
    }

    return(
        <AuthContext.Provider value={{
            customer,
            login
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);
export default AuthProvider;