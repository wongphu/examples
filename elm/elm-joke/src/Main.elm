module Main exposing (..)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import Json.Decode exposing(Decoder, field, string)

-- MAIN
main: Program () Model Msg
main =
  Browser.element
   { init = init
   , view = view
   , update = update
   , subscriptions = subscriptions
   }

-- MODEL
type alias Model =
  { joke : WebData Joke }

type RemoteData e a
  = NotAsked
  | Loading
  | Failure e
  | Success a 

type alias WebData a = RemoteData Http.Error a

type alias Joke = String


init : () -> ( Model, Cmd Msg )
init flags =
  ( { joke = NotAsked }
  , getJoke
  )

-- UPDATE
type Msg
  = GetJoke
  | GotJoke (WebData Joke)

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    GetJoke ->
      ( { model | joke = Loading }, getJoke )
    GotJoke joke -> 
      ( { model | joke = joke }, Cmd.none )

-- SUBSCRIPTIONS
subscriptions : Model -> Sub Msg
subscriptions _ = Sub.none

-- VIEW
view : Model -> Html Msg
view model =
  div [] 
    [ h1 [] [ text "Joke" ]
    , viewJoke model.joke
    , button [ onClick GetJoke ] [ text "more" ]
    ]


viewJoke : WebData Joke -> Html Msg
viewJoke joke =
  case joke of
    NotAsked -> div [] [ text "Initializing..." ]
    Loading -> div [] [ text "Loading..." ]
    Failure err -> div [] [ text ("Error: " ++ Debug.toString err) ]
    Success value -> div []  [ text value ]



-- HTTP
decodeJoke : Decoder Joke
decodeJoke = field "joke" string
  

getJoke : Cmd Msg
getJoke =
  Http.request
    { method = "GET"
    , headers = [ Http.header "Accept" "application/json" ]
    , url = "https://icanhazdadjoke.com"
    , body = Http.emptyBody
    , expect = Http.expectJson (GotJoke << fromResult) decodeJoke
    , timeout = Nothing
    , tracker = Nothing
    }

fromResult : (Result Http.Error a) -> WebData a
fromResult result =
  case result of
    Ok value -> Success value
    Err err -> Failure err

