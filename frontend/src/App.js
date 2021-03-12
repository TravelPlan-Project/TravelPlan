import "./App.css";
import MainPage from "./pages/mainPage/mainPage";
import SelectAreaPage from "./pages/selectAreaPage/selectAreaPage";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import UsersRoutePage from "./pages/usersRoutePage/usersRoutePage";
import HotplacePage from "./pages/hotPlacePage/hotplacePage";
import { RecoilRoot } from "recoil";

function App() {
  return (
    <RecoilRoot>
      <Router>
        <Route exact path="/">
          <MainPage />
        </Route>
        <Route path="/start">
          <SelectAreaPage />
        </Route>
        <Route exact path="/:id/places">
          <HotplacePage />
        </Route>
        <Route exact path="/:id/routes">
          <UsersRoutePage />
        </Route>
      </Router>
    </RecoilRoot>
  );
}

export default App;
